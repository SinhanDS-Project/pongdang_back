package com.wepong.pongdang.service;

import org.springframework.stereotype.Service;

import com.wepong.pongdang.entity.UserEntity;
import com.wepong.pongdang.entity.WalletEntity;
import com.wepong.pongdang.entity.enums.WalletType;
import com.wepong.pongdang.repository.WalletRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {

	private final WalletRepository walletRepository;

	public WalletEntity findByIdAndType(Long userId, WalletType type) {
		return walletRepository.findByUserIdAndWalletType(userId, type);
	}

	public void insertWallet(UserEntity user) {
		WalletEntity pongWallet = WalletEntity.builder()
			.walletType(WalletType.PONG)
			.user(user)
			.build();

		WalletEntity donaWallet = WalletEntity.builder()
			.walletType(WalletType.DONA)
			.user(user)
			.build();

		walletRepository.save(pongWallet);
		walletRepository.save(donaWallet);
	}

	public void addPong(int point, Long userId) {
		WalletEntity pongWallet = walletRepository.findByUserIdAndWalletType(userId, WalletType.PONG);
		pongWallet.setPongBalance(pongWallet.getPongBalance() + point);
		walletRepository.save(pongWallet);
	}

	public void losePong(int point, Long userId) {
		WalletEntity pongWallet = walletRepository.findByUserIdAndWalletType(userId, WalletType.PONG);
		pongWallet.setPongBalance(pongWallet.getPongBalance() - point);
		walletRepository.save(pongWallet);
	}

	public void addDona(int point, Long userId) {
		WalletEntity pongWallet = walletRepository.findByUserIdAndWalletType(userId, WalletType.DONA);
		pongWallet.setPongBalance(pongWallet.getPongBalance() + point);
		walletRepository.save(pongWallet);
	}

	public void loseDona(int point, Long userId) {
		WalletEntity pongWallet = walletRepository.findByUserIdAndWalletType(userId, WalletType.DONA);
		pongWallet.setPongBalance(pongWallet.getPongBalance() - point);
		walletRepository.save(pongWallet);
	}
}
