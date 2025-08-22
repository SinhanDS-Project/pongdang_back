package com.wepong.pongdang.repository;

import com.wepong.pongdang.entity.GameRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoomEntity, String> {
//    Page<GameRoom> findAll(Pageable pageable);
}