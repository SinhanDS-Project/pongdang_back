package com.wepong.pongdang.service;

import com.wepong.pongdang.dto.request.LoginRequestDTO;
import com.wepong.pongdang.dto.request.UserRegisterDTO;
import com.wepong.pongdang.dto.request.UserUpdateRequestDTO;
import com.wepong.pongdang.entity.AuthTokenEntity;
import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.exception.*;
import com.wepong.pongdang.model.aws.S3FileServiceReturnKey;
import com.wepong.pongdang.repository.TokenRepository;
import com.wepong.pongdang.repository.UserRepository;
import com.wepong.pongdang.repository.WalletRepository;
import com.wepong.pongdang.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final WalletRepository walletRepository;
	private final WalletService walletService;

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private S3FileServiceReturnKey s3FileService;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	// 로그인 요청 검증
	public Map<String, String> login(LoginRequestDTO request) {
		Map<String, String> responseToken = new HashMap<>();
		
		UserEntity userEntity = userRepository.findByEmail(request.getEmail());
		AuthTokenEntity token = tokenRepository.findByUserId(userEntity.getId());

		if (userEntity == null) {
			throw new UserNotFoundException();
		} else if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
			throw new InvalidPasswordException();
		}


		String accessToken = jwtUtil.generateAccessToken(userEntity.getId());
		String refreshToken = jwtUtil.generateRefreshToken(userEntity.getId());

		if (token == null) {
			token = AuthTokenEntity.builder()
					.user(userEntity)
					.refreshToken(refreshToken)
					.build();
		} else {
			token.updateRefreshToken(refreshToken);
		}

		tokenRepository.save(token);
		
		responseToken.put("accessToken", accessToken);
		responseToken.put("refreshToken", refreshToken);
		
		return responseToken;
	}

	public String reissue(String refreshToken) {
		if (!jwtUtil.validateToken(refreshToken)) {
			throw new InvalidTokenException();
		}

		Long userId = jwtUtil.getUserIdFromToken(refreshToken);
		return jwtUtil.generateAccessToken(userId);
	}

	public UserEntity findById(Long id) {
		UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
		return userEntity;
	}

	public Long validateAndGetUserId(String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new InvalidTokenException();
		}
		String token = authHeader.substring(7);
		if (!jwtUtil.validateToken(token)) {
			throw new SessionExpiredException();
		}
		return jwtUtil.getUserIdFromToken(token);
	}
	
	// 이메일 중복 검사
	public boolean isEmailExists(String email) {
		return userRepository.countByEmail(email) > 0;
	}
	
	// 닉네임 중복 검사
	public boolean isNicknameExists(String nickname) {
		return userRepository.countByNickname(nickname) > 0;
	}

	public boolean isPhoneNumberExists(String phone_number) {
		return userRepository.countByPhoneNumber(phone_number) > 0;
	}

	public void register(UserRegisterDTO dto) {
	// String → java.sql.Date 변환
    Date birthDate = null;
    try {
        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getBirthDate());
        birthDate = new Date(utilDate.getTime());
    } catch (ParseException e) {
        throw new IllegalArgumentException("생년월일 형식이 올바르지 않습니다. (yyyy-MM-dd)", e);
    }
    
		UserEntity userEntity = UserEntity.builder()
				.userName(dto.getUserName())
				.password(passwordEncoder.encode(dto.getPassword()))
				.nickname(dto.getNickname())
				.email(dto.getEmail())
				.birthDate(birthDate)
				.phoneNumber(dto.getPhoneNumber())
				.agreePrivacy(dto.isAgreePrivacy())
				.build();

		userRepository.save(userEntity);

		walletService.insertWallet(userEntity);
	}

	public void updateUser(UserUpdateRequestDTO userRequest, Long userId) {
		// 기존 정보 조회
		UserEntity existingUserEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
	    
	    // 🔐 현재 비밀번호 확인
	    if (!passwordEncoder.matches(userRequest.getPassword(), existingUserEntity.getPassword())) {
	        throw new InvalidUpdatePasswordException();
	    }
	    
	    // 🔒 새 비밀번호가 들어온 경우 암호화 후 저장
	    if (userRequest.getNewPassword() != null && !userRequest.getNewPassword().isBlank()) {
	        String encodedNewPassword = passwordEncoder.encode(userRequest.getNewPassword());
	        existingUserEntity.setPassword(encodedNewPassword);
	    } else {
	    	// 새 비밀번호를 입력하지 않았다면 기존 비밀번호를 유지
	        existingUserEntity.setPassword(existingUserEntity.getPassword());
	    }
		
	    // 📱 전화번호: 무조건 수정 (빈 문자열이면 그대로 저장됨)
	    existingUserEntity.setPhoneNumber(userRequest.getPhoneNumber());
	    
	    // 🎂 생년월일: null이 아니면 수정
	    if (userRequest.getBirthDate() != null) {
	        existingUserEntity.setBirthDate(userRequest.getBirthDate());
	    }
	    
	    // ✅ 프로필 이미지 처리
	    MultipartFile newImage = userRequest.getProfileImage();
	    String oldUrl = userRequest.getProfileImgUrl();
	    if (newImage != null && !newImage.isEmpty()) {
	        if (oldUrl != null && !oldUrl.isBlank()) {
		        // 기존 이미지가 있다면 S3에서 삭제
	            String key = extractObjectKeyFromUrl(oldUrl);
	            s3FileService.deleteObject(key);
	        }

	        // 새 이미지 업로드
	        String newUrl = s3FileService.uploadFile(newImage);
	        existingUserEntity.setProfileImage(newUrl);
	    } else {
	    	// 이미지 변경 안 했다면
	    	oldUrl = extractObjectKeyFromUrl(oldUrl);
	    	existingUserEntity.setProfileImage(oldUrl != null ? oldUrl : "");
	    }
		userRepository.save(existingUserEntity);
	}
	
	private String extractObjectKeyFromUrl(String url) {
	    if (url == null || url.isBlank()) return null;

	    // https://your-bucket.s3.amazonaws.com/images/folder/file.png
	    int index = url.indexOf(".amazonaws.com/");
	    if (index == -1) return null;

	    // object key 부분만 추출
	    return url.substring(index + ".amazonaws.com/".length());
	}

	public void logout(Long userId) {
		AuthTokenEntity token = tokenRepository.findByUserId(userId);
		tokenRepository.delete(token);
	}

	public String getUserEmail(String userName, String phoneNumber) {
		String email = userRepository.findByUserNameAndPhoneNumber(userName, phoneNumber).getEmail();
		return email;
	}

	public void updatePassword(Long userId, String password) {
		UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
		userEntity.updatePassword(passwordEncoder.encode(password));
		userRepository.save(userEntity);
	}

	public UserEntity findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
