package com.wepong.pongdang.entity;

import com.wepong.pongdang.entity.enums.QAMainType;
import com.wepong.pongdang.entity.enums.QASubType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "chatbot_qa")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatBotQAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private QAMainType mainCategory;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private QASubType subCategory;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String questionText;

    @Column(columnDefinition = "TEXT")
    private String answerText;
}
