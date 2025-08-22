package com.wepong.pongdang.controller;

import com.wepong.pongdang.dto.response.HistoryResponseDTO;
import com.wepong.pongdang.entity.GameHistoryEntity;
import com.wepong.pongdang.entity.PongHistoryEntity;
import com.wepong.pongdang.service.AuthService;
import com.wepong.pongdang.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class HistoryRestController {

    @Autowired
    HistoryService historyService;
    @Autowired
    AuthService authService;

    @GetMapping("/game/list")
    public HistoryResponseDTO.GameResponseDTO gameHistoryList(
    											@RequestHeader("Authorization") String authHeader,
                                                @RequestParam(defaultValue = "1") int page) {
        String userId = authService.validateAndGetUserId(authHeader);
        return historyService.gameHistoryList(userId, page);
    }

    @GetMapping("/point/list")
    public HistoryResponseDTO.PointResponseDTO pointHistoryList(
    											@RequestHeader("Authorization") String authHeader,
                                                @RequestParam(defaultValue = "1") int page) {
        String userId = authService.validateAndGetUserId(authHeader);
        return historyService.pointHistoryList(userId, page);
    }

    @PostMapping("/game/insert")
    public ResponseEntity<?> insertGameHistory(@RequestBody GameHistoryEntity gameRequest,
                                            @RequestHeader("Authorization") String authHeader) {
        String userId = authService.validateAndGetUserId(authHeader);
        historyService.insertGameHistory(gameRequest, userId);

        return ResponseEntity.ok("게임 히스토리 저장이 완료되었습니다.");
    }

    @PostMapping("/point/insert")
    public ResponseEntity<?> insertPointHistory(@RequestBody PongHistoryEntity pointRequest,
                                    @RequestHeader("Authorization") String authHeader) {
        String userId = authService.validateAndGetUserId(authHeader);
        historyService.insertPointHistory(pointRequest, userId);

        return ResponseEntity.ok("포인트 히스토리 저장이 완료되었습니다.");
    }
}