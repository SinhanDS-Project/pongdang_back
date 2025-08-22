package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import com.wepong.pongdang.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity(name = "user") // 테이블 이름
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id // PK 설정
    @Column(name = "uid", columnDefinition = "CHAR(32)") // 칼럼 속성 설정
    private String uid;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String userName;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String password;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String nickname;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)", unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)", unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean agreePrivacy;

    @Column
    @ColumnDefault("0")
    private int pointBalance;

    @Column(columnDefinition = "VARCHAR(225) DEFAULT ''")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private LocalDateTime lastLoginAt;

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void addPoint(int point) {
        this.pointBalance += point;
    }

    public void losePoint(int point) {
        this.pointBalance -= point;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
