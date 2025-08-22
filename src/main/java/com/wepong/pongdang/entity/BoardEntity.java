package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.common.BaseEntity;
import com.wepong.pongdang.entity.enums.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "board")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardEntity extends BaseEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int viewCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int likeCount;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String boardImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private UserEntity userEntity;

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }
}
