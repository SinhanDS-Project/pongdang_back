package com.wepong.pongdang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wepong.pongdang.entity.RewardPerResultEntity;
import com.wepong.pongdang.entity.enums.RankType;

@Repository
public interface RewardPerResultRepository extends JpaRepository<RewardPerResultEntity, Long> {
	RewardPerResultEntity findByGameLevelIdAndRank(Long gameLevelId, RankType rank);
}
