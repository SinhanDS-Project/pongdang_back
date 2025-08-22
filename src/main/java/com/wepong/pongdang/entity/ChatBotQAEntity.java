package com.wepong.pongdang.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "chatbot_qa")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatBotQAEntity {

    @Id
    @Column(columnDefinition = "CHAR(32)")
    private String uid;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String mainCategory;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String subCategory;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String questionText;

    @Column(columnDefinition = "TEXT")
    private String answerText;
}
