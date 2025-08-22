package com.wepong.pongdang.controller;

import com.wepong.pongdang.dto.response.BannerResponseDTO;
import com.wepong.pongdang.dto.response.BettubeResponseDTO;
import com.wepong.pongdang.entity.BannerEntity;
import com.wepong.pongdang.entity.BettubeEntity;
import com.wepong.pongdang.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/content")
public class ContentRestController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/banner/list")
    public BannerResponseDTO.BannerListDTO bannerList() {
        List<BannerEntity> list = contentService.bannerList();
        List<BannerResponseDTO.BannerDetailDTO> details = list.stream()
                .map(BannerResponseDTO.BannerDetailDTO::from)
                .collect(Collectors.toList());
        return BannerResponseDTO.BannerListDTO.builder().banners(details).build();
    }

    @GetMapping("/bettube/list")
    public BettubeResponseDTO.BettubeListDTO bettubeList() {
        List<BettubeEntity> list = contentService.bettubeList();
        List<BettubeResponseDTO.BettubeDetailDTO> details = list.stream()
                .map(BettubeResponseDTO.BettubeDetailDTO::from)
                .collect(Collectors.toList());
        return BettubeResponseDTO.BettubeListDTO.builder().bettubes(details).build();
    }
}
