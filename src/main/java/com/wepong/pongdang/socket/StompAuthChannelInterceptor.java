package com.wepong.pongdang.socket;

import com.wepong.pongdang.exception.InvalidTokenException;
import com.wepong.pongdang.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final AuthService authService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // STOMP 연결 요청 시
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 헤더에서 토큰 추출 후 검증
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if(authHeader == null) throw new InvalidTokenException();

            Long userId = authService.validateAndGetUserId(authHeader);

            // Principal 구현
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, List.of());
            accessor.setUser(auth);
        }

        // 메시지 그대로 컨트롤러에 반환
        return message;
    }
}
