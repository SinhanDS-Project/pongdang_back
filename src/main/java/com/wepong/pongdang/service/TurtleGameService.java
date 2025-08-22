package com.wepong.pongdang.service;

import com.wepong.pongdang.dto.response.GameRoomResponseDTO;
import com.wepong.pongdang.dto.response.TurtlePlayerDTO;
import com.wepong.pongdang.entity.GameEntity;
import com.wepong.pongdang.entity.GameHistoryEntity;
import com.wepong.pongdang.entity.PointHistoryEntity;
import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.entity.enums.GameResult;
import com.wepong.pongdang.entity.enums.Level;
import com.wepong.pongdang.entity.enums.PointHistoryType;
import com.wepong.pongdang.model.multi.turtle.PlayerDAO;
import com.wepong.pongdang.model.multi.turtle.TurtleGameState;
import com.wepong.pongdang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TurtleGameService {
	
	@Autowired
	private PlayerDAO playerDAO;
	@Autowired
	private GameRoomService gameRoomService;
	@Autowired
	private AuthService authService;
	@Autowired
	private GameService gameService;
	@Autowired
	private HistoryService historyService;

    private final UserRepository userRepository;
	
    private final Map<String, TurtleGameState> gameStates = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final Map<String, List<TurtlePlayerDTO>> gameStartPlayersMap = new ConcurrentHashMap<>();

    // 콜백 인터페이스(핸들러에서 정의)
    public interface RaceUpdateCallback {
        void onRaceUpdate(String roomId, double[] positions);
        void onRaceFinish(String roomId, int winner, List<Map<String, Object>> results);
    }

    // 게임 시작(레이스 시작)
    public void startGame(String roomId, int turtleCount, RaceUpdateCallback callback) {
        TurtleGameState state = new TurtleGameState(turtleCount);
        gameStates.put(roomId, state);
        List<TurtlePlayerDTO> startPlayers = playerDAO.getAll(roomId);
		// null 방지
		if (startPlayers != null) {
			gameStartPlayersMap.put(roomId, new ArrayList<>(startPlayers));
		}
        scheduler.schedule(() -> runRaceLoop(roomId, state, callback), 0, TimeUnit.SECONDS);
    }

    // 실제 레이스 루프(40ms마다)
    private void runRaceLoop(String roomId, TurtleGameState state, RaceUpdateCallback callback) {
        int interval = 30;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (state.isFinished()) return;
                state.updateRace();
                callback.onRaceUpdate(roomId, state.getPositions());
                if (state.isFinished()) {
                	List<Map<String, Object>> results = gameResultAndPointCalc(roomId, state);
                    callback.onRaceFinish(roomId, state.getWinner(), results);
                } else {
                    scheduler.schedule(this, interval, TimeUnit.MILLISECONDS);
                }
            }
        };
        scheduler.schedule(task, 0, TimeUnit.MILLISECONDS);
    }
    
    // 결과에 따른 포인트, 승패 계산
    private List<Map<String, Object>> gameResultAndPointCalc(String roomId, TurtleGameState state) {
        List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);
    	List<TurtlePlayerDTO> startPlayers = gameStartPlayersMap.get(roomId);
        GameRoomResponseDTO.GameRoomDetailDTO gameroom = gameRoomService.selectById(roomId);
        String gameName = "Turtle Run";
        Level difficulty = gameroom.getLevel();
        int winner = state.getWinner();

        double userRate = 1.0;
        switch(difficulty) {
            case EASY: userRate = 0.2; break;
            case NORMAL: userRate = 1.5; break;
            case HARD: userRate = 4.0; break;
        }
        int winnerTurtle = winner; // 0-based

        int totalBet = startPlayers.stream().mapToInt(TurtlePlayerDTO::getBettingPoint).sum();
        int winPool = players.stream()
            .filter(p -> p.getTurtleId() != null && Integer.parseInt(p.getTurtleId()) - 1 == winnerTurtle)
            .mapToInt(TurtlePlayerDTO::getBettingPoint)
            .sum();
        if (winPool == 0) winPool = 1; // 0방지

        // 결과 리스트 준비
        List<Map<String, Object>> results = new ArrayList<>();

        for (TurtlePlayerDTO player : players) {
            int selectedTurtle = player.getTurtleId() != null ? Integer.parseInt(player.getTurtleId()) - 1 : -1;
            int userBet = player.getBettingPoint();
            int winAmount = 0;
            int pointChange = 0;
            boolean didWin = (selectedTurtle == winnerTurtle);

            if (didWin) {
                winAmount = (int)Math.round(((double)totalBet / winPool) * userBet + userBet * userRate);
                pointChange += winAmount;
            } else {
                winAmount = 0;
                pointChange -= userBet;
            }
            String gameResult = didWin ? "WIN" : "LOSE";
            saveTurtleRunResult(player.getUserUid(), userBet, winAmount, gameResult, gameName);

            // ✅ 각 플레이어 결과 Map 저장
            Map<String, Object> result = new HashMap<>();
            result.put("user_uid", player.getUserUid());
            result.put("selectedTurtle", selectedTurtle);
            result.put("didWin", didWin);
            result.put("winAmount", winAmount);
            result.put("pointChange", pointChange);
            results.add(result);
        }
        return results;
    }
    
	 // DB 저장
    private void saveTurtleRunResult(String userUid, int betAmount, int winAmount, String gameResult, String gameName) {
    	UserEntity userEntity = authService.findByUid(userUid);
        if (userEntity != null) {
        	 if ("WIN".equals(gameResult)) {
     	        userEntity.setPointBalance(userEntity.getPointBalance() + winAmount);
     	    } else {
     	        // 이미 차감된 상태면 생략, 아니면 아래 라인 활성화
     	         userEntity.setPointBalance(userEntity.getPointBalance() - betAmount);
     	    }
     	    userRepository.save(userEntity);
            // 히스토리/포인트 히스토리 등도 기록
            String gameUid = gameService.selectByName(gameName)
            		.stream().findFirst()
            		.orElseThrow(() -> new IllegalStateException("'" + gameName + "' 게임을 찾을 수 없습니다."))
            		.getUid();

            GameEntity gameEntity = gameService.selectById(gameUid);

            // 게임 히스토리 저장 (gameName도 같이)
            GameHistoryEntity gameHistoryEntity = GameHistoryEntity.builder()
                    .gameEntity(gameEntity)
                    .bettingAmount(betAmount)
                    .pointValue(Math.abs(winAmount - betAmount))
                    .gameResult(GameResult.valueOf(gameResult))
                    .build();

            historyService.insertGameHistory(gameHistoryEntity, userUid);

            // 포인트 히스토리 저장
            PointHistoryEntity pointHistoryEntity = PointHistoryEntity.builder()
                    .gameHistoryEntity(gameHistoryEntity)
                    .type(PointHistoryType.valueOf(gameResult))
                    .amount(Math.abs(winAmount - betAmount))
                    .balanceAfter(userEntity.getPointBalance())
                    .build();

            historyService.insertPointHistory(pointHistoryEntity, userUid);
        }
    }
    
    public TurtleGameState getGameState(String roomId) {
        return gameStates.get(roomId);
    }
    public void removeGame(String roomId) {
        gameStates.remove(roomId);
    }
}