package com.wepong.pongdang.controller;

import com.wepong.pongdang.exception.UserNotFoundException;
import com.wepong.pongdang.service.AuthService;
import com.wepong.pongdang.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class VerificationRestController {
	@Autowired
	private AuthService authService;

	@Autowired
	private VerificationService verificationService;

	@PostMapping(value = "/request", produces = "text/plain;charset=utf-8")
	public ResponseEntity<?> requestVerification(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		
		if(!authService.isEmailExists(email)) {
			verificationService.requestVerification(email);
			return ResponseEntity.ok("인증번호가 이메일로 발송되었습니다.");
		}
		
		return ResponseEntity.ok("이미 가입된 이메일입니다.");
	}

	@PostMapping(value = "/find/request", produces = "text/plain;charset=utf-8")
	public ResponseEntity<?> findVerification(@RequestBody Map<String, String> request) {
		String email = request.get("email");

		if(!authService.isEmailExists(email)) {
			throw new UserNotFoundException();
		} else {
			verificationService.requestVerification(email);
			return ResponseEntity.ok("인증번호가 이메일로 발송되었습니다.");
		}
	}

	@PostMapping(value = "/verify", produces = "text/plain;charset=utf-8")
	public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String code = request.get("code");
		verificationService.verifyCode(email, code);
		return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
	}

	@PostMapping(value = "/password", produces = "text/plain;charset=utf-8")
	public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String userId = authService.findByEmail(email).getUid();
		verificationService.updatePassword(email, userId);
		return ResponseEntity.ok("임시 비밀번호가 이메일로 발송되었습니다.");
	}
}