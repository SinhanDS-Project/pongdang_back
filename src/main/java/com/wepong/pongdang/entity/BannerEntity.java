package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "banner")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BannerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String imagePath;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String bannerLinkUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
