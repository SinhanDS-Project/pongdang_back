package com.wepong.pongdang.socket;

import com.wepong.pongdang.dto.response.GameRoomResponseDTO;
import com.wepong.pongdang.dto.response.TurtlePlayerDTO;
import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.entity.enums.GameRoomStatus;
import com.wepong.pongdang.model.multi.turtle.PlayerDAO;
import com.wepong.pongdang.model.multi.turtle.SessionService;
import com.wepong.pongdang.service.AuthService;
import com.wepong.pongdang.service.GameRoomService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 웹소켓 메시지 처리
@Component
public class GameRoomWebSocketHandler extends TextWebSocketHandler {

	// 스프링 빈 사용
	@Autowired
	private PlayerDAO playerDAO;
	@Autowired
	private SessionService sessionService;
    @Autowired
    private GameRoomService gameRoomService;
    @Autowired
    private AuthService authService;
	@Autowired
	private GameRoomListWebSocket gameRoomListWebSocket;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 연결된 세션 저장, 초기 데이터 전송 등
		String userId = (String) session.getAttributes().get("userId");
		String roomId = (String) session.getAttributes().get("roomId");

		UserEntity userEntity = authService.findByUid(userId);
		GameRoomResponseDTO.GameRoomDetailDTO gameroom = gameRoomService.selectById(roomId);

		if(roomId == null || userId == null) {
			session.close(CloseStatus.BAD_DATA);
			return;
		}

		// 플레이어 리스트 조회
		List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);

		if(players != null) {
			// 중복 입장 검사
			for(TurtlePlayerDTO player : players) {
				if(player.getUserUid().equals(userId)) {
					sessionService.removeSession(roomId, userId);
					break;
				}
			}
		}

		// 세션 등록
		sessionService.addSession(roomId, session);

		TurtlePlayerDTO existingPlayer = playerDAO.getPlayer(roomId, userId);

		if(existingPlayer == null) {
			// 플레이어 추가
			TurtlePlayerDTO player = TurtlePlayerDTO.builder()
				.userUid(userId)
				.nickname(userEntity.getNickname())
				.roomUid(roomId)
				.isReady(false)
				.bettingPoint(gameroom.getMinBet())
				.build();

			playerDAO.addPlayer(roomId, player);
			gameRoomListWebSocket.broadcastMessage("enter");
		}

		// 입장 메시지 방송
		Map<String, Object> data = new HashMap<>();
		data.put("userId", userEntity.getNickname());
		broadcastMessage("enter", roomId, data);
	}
	
	private void broadcastMessage(String type, String roomId, Map<String, Object> data) throws IOException {
		// 웹소켓 메시지 전송
		List<WebSocketSession> sessions = sessionService.getSessions(roomId);
		List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);

		if (sessions == null || sessions.isEmpty()) {
			// 더 이상 메시지 보낼 대상이 없음
			return;
		}

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> messageMap = new HashMap<>();
		messageMap.put("type", type);
		messageMap.put("players", players);

		if (data != null) {
			messageMap.putAll(data);
		}

		String jsonMessage = mapper.writeValueAsString(messageMap);

		for (WebSocketSession session : sessions) {
			synchronized (session) {
				if (session.isOpen()) {
					session.sendMessage(new TextMessage(jsonMessage));
				}
			}
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 클라이언트가 보낸 메시지를 받아서 처리 (예: 채팅, 입장, 베팅 등)

		// 메시지 파싱
		String payload = message.getPayload();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(payload);

		String type = json.get("type").asText();

		String roomId = (String) session.getAttributes().get("roomId");
		String userId = (String) session.getAttributes().get("userId");

		TurtlePlayerDTO player = playerDAO.getPlayer(roomId, userId);

		// 메시지 타입에 따라 분기
		switch(type) {
			case "choice":
				String turtleId = json.get("turtle_id").asText();
				player.setTurtleId(turtleId);
				break;
			case "betting":
				int bettingPoint = json.get("betting_point").asInt();
				player.setBettingPoint(bettingPoint);
				break;
			case "ready":
				Boolean isReady = json.get("isReady").asBoolean();
				player.setReady(isReady);
				break;
			case "start":
				Map<String, Object> data = new HashMap<>();
				data.put("target", "/multi/" + roomId + "/turtlerun");
				broadcastMessage("start", roomId, data);
				return;
		}
		List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);

		Map<String, Object> data = new HashMap<>();
		data.put("players", players);
		broadcastMessage("update", roomId, data);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 세션 제거, 퇴장 처리 등
		String roomId = (String) session.getAttributes().get("roomId");
		String userId = (String) session.getAttributes().get("userId");

		sessionService.removeSession(roomId, session);

		UserEntity userEntity = authService.findByUid(userId);

		GameRoomResponseDTO.GameRoomDetailDTO gameroom = gameRoomService.selectById(roomId);
		GameRoomStatus gameStatus = gameroom.getStatus();

		Map<String, Object> data = new HashMap<>();

		if(!gameStatus.equals(GameRoomStatus.PLAYING)) {
			playerDAO.removePlayer(roomId, userId);

			if(userId.equals(gameroom.getHostUid())) {
				List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);

				if(players != null && !players.isEmpty()) {
					gameRoomService.updateHost(roomId, players.get(0).getUserUid());
					data.put("hostId", players.get(0).getUserUid());
				} else {
					// 플레이어가 0명일 때 방 삭제
					gameRoomService.deleteRoom(roomId);
					gameRoomListWebSocket.broadcastMessage("delete");
				}
			}

			gameRoomListWebSocket.broadcastMessage("exit");

			data.put("userId", userEntity.getNickname());
			broadcastMessage("exit", roomId, data);
		}

		// 플레이어가 0명일 때 방 삭제
		List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);
		if (players == null || players.isEmpty()) {
			gameRoomService.deleteRoom(roomId);
			gameRoomListWebSocket.broadcastMessage("delete");
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// 예외 로깅 또는 복구 처리
	}
}