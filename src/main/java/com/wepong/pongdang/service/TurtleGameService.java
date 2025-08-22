package com.wepong.pongdang.service;

import com.wepong.pongdang.dto.response.GameRoomResponseDTO;
import com.wepong.pongdang.dto.response.TurtlePlayerDTO;
import com.wepong.pongdang.entity.GameEntity;
import com.wepong.pongdang.entity.GameHistoryEntity;
import com.wepong.pongdang.entity.GameLevelEntity;
import com.wepong.pongdang.entity.GameRoomEntity;
import com.wepong.pongdang.entity.PongHistoryEntity;
import com.wepong.pongdang.entity.RewardPerResultEntity;
import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.entity.WalletEntity;
import com.wepong.pongdang.entity.enums.PongHistoryType;
import com.wepong.pongdang.entity.enums.RankType;
import com.wepong.pongdang.entity.enums.WalletType;
import com.wepong.pongdang.model.multi.turtle.PlayerDAO;
import com.wepong.pongdang.model.multi.turtle.TurtleGameState;
import com.wepong.pongdang.repository.GameRoomRepository;
import com.wepong.pongdang.repository.RewardPerResultRepository;
import com.wepong.pongdang.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class TurtleGameService {
	
	@Autowired
	private PlayerDAO playerDAO;

	private final GameRoomService gameRoomService;
	private final AuthService authService;
	private final GameService gameService;
	private final HistoryService historyService;

    private final UserRepository userRepository;
	private final RewardPerResultRepository rewardPerResultRepository;
	
    private final Map<Long, TurtleGameState> gameStates = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final Map<Long, List<TurtlePlayerDTO>> gameStartPlayersMap = new ConcurrentHashMap<>();
	@Autowired
	private WalletService walletService;
	@Autowired
	private GameLevelService gameLevelService;
	private GameRoomRepository gameRoomRepository;

	// 콜백 인터페이스(핸들러에서 정의)
    public interface RaceUpdateCallback {
        void onRaceUpdate(Long roomId, double[] positions);
        void onRaceFinish(Long roomId, int winner, List<Map<String, Object>> results);
    }

    // 게임 시작(레이스 시작)
    public void startGame(Long roomId, int turtleCount, RaceUpdateCallback callback) {
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
    private void runRaceLoop(Long roomId, TurtleGameState state, RaceUpdateCallback callback) {
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
    private List<Map<String, Object>> gameResultAndPointCalc(Long roomId, TurtleGameState state) {
        List<TurtlePlayerDTO> players = playerDAO.getAll(roomId);
        GameRoomResponseDTO.GameRoomDetailDTO gameroom = gameRoomService.selectById(roomId);
        String gameName = gameroom.getGameName();
        int winner = state.getWinner();

		GameRoomEntity gameRoomEntity = gameRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("게임방이 존재하지 않습니다."));
		GameLevelEntity gameLevel = gameRoomEntity.getGameLevel();

		Long gameLevelId = gameRoomEntity.getGameLevel().getId();
		int entryFee = gameLevel.getEntryFee();

        // 결과 리스트 준비
        List<Map<String, Object>> results = new ArrayList<>();

        for (TurtlePlayerDTO player : players) {
            int selectedTurtle = player.getTurtleId() != null ? Integer.parseInt(player.getTurtleId()) - 1 : -1;
            boolean didWin = (selectedTurtle == winner);

			RankType gameResult = didWin ? RankType.WIN : RankType.LOSE;

			RewardPerResultEntity rewardConfig = rewardPerResultRepository.findByGameLevelIdAndRank(gameLevelId, gameResult);

			int reward = didWin ? rewardConfig.getReward() : 0;
			int donation = didWin ? rewardConfig.getDonation() : 0;
			int pongChange = didWin ? reward : -entryFee;

            saveTurtleRunResult(player.getUserId(), entryFee, reward, donation, gameResult, gameName);

            // ✅ 각 플레이어 결과 Map 저장
            Map<String, Object> result = new HashMap<>();
            result.put("user_uid", player.getUserId());
            result.put("selectedTurtle", selectedTurtle);
            result.put("didWin", didWin);
            result.put("winAmount", reward);
            result.put("pointChange", pongChange);
            results.add(result);
        }
        return results;
    }
    
	 // DB 저장
    private void saveTurtleRunResult(Long userId, int entryFee, int reward, int donation, RankType gameResult, String gameName) {
    	UserEntity userEntity = authService.findById(userId);
		WalletEntity pongWallet = walletService.findByIdAndType(userId, WalletType.PONG);
		WalletEntity donaWallet = walletService.findByIdAndType(userId, WalletType.DONA);
        if (userEntity != null) {
        	 if (gameResult.equals(RankType.WIN)) {
				 pongWallet.setPongBalance(pongWallet.getPongBalance() + reward);
				 donaWallet.setPongBalance(donaWallet.getPongBalance() + donation);
     	    } else {
				 // 이미 차감된 상태면 생략, 아니면 아래 라인 활성화
				 pongWallet.setPongBalance(pongWallet.getPongBalance() - entryFee);
     	    }
     	    userRepository.save(userEntity);
            // 히스토리/포인트 히스토리 등도 기록
            Long gameId = gameService.selectByName(gameName)
            		.stream().findFirst()
            		.orElseThrow(() -> new IllegalStateException("'" + gameName + "' 게임을 찾을 수 없습니다."))
            		.getId();

            GameEntity gameEntity = gameService.selectById(gameId);

            // 게임 히스토리 저장 (gameName도 같이)
            GameHistoryEntity gameHistoryEntity = GameHistoryEntity.builder()
                    .game(gameEntity)
                    .entryFee(entryFee)
                    .pongValue(reward)
                    .rank(gameResult)
                    .build();

            historyService.insertGameHistory(gameHistoryEntity, userId);

            // 포인트 히스토리 저장
            PongHistoryEntity pongHistoryEntity = PongHistoryEntity.builder()
                    .type(PongHistoryType.GAME)
                    .amount(Math.abs(reward - entryFee))
                    .build();

            historyService.insertPointHistory(pongHistoryEntity, userId);
        }
    }
    
    public TurtleGameState getGameState(Long roomId) {
        return gameStates.get(roomId);
    }
    public void removeGame(Long roomId) {
        gameStates.remove(roomId);
    }
}