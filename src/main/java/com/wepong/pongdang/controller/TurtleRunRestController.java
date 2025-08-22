package com.wepong.pongdang.controller;

import com.wepong.pongdang.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/multi")
public class TurtleRunRestController {

	@Autowired
	private AuthService authService;
	
	// 웹소켓 연결을 위한 정보 받기
	@GetMapping("/gameroom/detail/{roomId}/info")
	public Map<String, Object> getRoomInfo(@PathVariable String roomId, @RequestHeader("Authorization") String authHeader) {
		Map<String, Object> map = new HashMap<>();
		String userId = authService.validateAndGetUserId(authHeader);
		
		map.put("roomId", roomId);
		map.put("userId", userId);
		
		return map;
	}
}

