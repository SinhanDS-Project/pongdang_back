package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    int countByEmail(String email);

    int countByNickname(String nickname);

    int countByPhoneNumber(String phoneNumber);

    UserEntity findByUserNameAndPhoneNumber(String userName, String phoneNumber);
}
