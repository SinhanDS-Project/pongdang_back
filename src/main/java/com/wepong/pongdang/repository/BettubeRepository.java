package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.BettubeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BettubeRepository extends JpaRepository<BettubeEntity, String> {
}
