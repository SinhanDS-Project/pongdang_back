package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthTokenEntity, Long> {
    AuthTokenEntity findByUserId(Long userId);
}
