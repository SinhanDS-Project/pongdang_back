package com.wepong.pongdang.dto.response;

import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.model.aws.S3ImagePathDeserializer;
import com.wepong.pongdang.model.aws.S3ImageUrlSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String uid;
    private String userName;
    private String nickname;
    private String email;
    private Date birthDate;
    private String phoneNumber;
    private int pointBalance;
    @JsonSerialize(using = S3ImageUrlSerializer.class)
    @JsonDeserialize(using = S3ImagePathDeserializer.class)
    private String profileImg;

    public static UserResponseDTO from(UserEntity userEntity, String profileImg) {
        return UserResponseDTO.builder()
                .uid(userEntity.getUid())
                .nickname(userEntity.getNickname())
                .userName(userEntity.getUserName())
                .birthDate(userEntity.getBirthDate())
                .email(userEntity.getEmail())
                .pointBalance(userEntity.getPointBalance())
                .profileImg(profileImg)
                .phoneNumber(userEntity.getPhoneNumber())
                .build();
    }
}
