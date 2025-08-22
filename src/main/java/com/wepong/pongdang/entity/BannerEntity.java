package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "banner")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BannerEntity extends BaseEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String imagePath;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String bannerLinkUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
}
