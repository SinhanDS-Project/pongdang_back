package com.wepong.pongdang.service;

import com.wepong.pongdang.entity.BannerEntity;
import com.wepong.pongdang.entity.BettubeEntity;
import com.wepong.pongdang.repository.BannerRepository;
import com.wepong.pongdang.repository.BettubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {

    private final BannerRepository bannerRepository;
    private final BettubeRepository bettubeRepository;

    public List<BannerEntity> bannerList() {
        return bannerRepository.findAll();
    }

    public List<BettubeEntity> bettubeList() {
        return bettubeRepository.findAll();
    }
}
