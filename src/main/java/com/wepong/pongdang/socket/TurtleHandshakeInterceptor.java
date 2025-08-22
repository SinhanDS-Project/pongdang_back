package com.wepong.pongdang.socket;

import com.wepong.pongdang.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

//HttpSession의 정보 WebSocketSession에 저장
@Component
public class TurtleHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    AuthService authService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler webSocketHandler,
                                   Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

            // 입장하는 게임방 저장
         	// 쿼리 파라미터에서 roomId 추출
            String requestURI = servletRequest.getRequestURI(); // 전체 URI

            // 마지막 경로 부분만 추출
            String[] parts = requestURI.split("/");
            Long roomId = Long.valueOf(parts[parts.length - 1]);

            attributes.put("roomId", roomId);

         // 토큰 추출
            String authHeader = "Bearer " + servletRequest.getParameter("token");
            Long userId = authService.validateAndGetUserId(authHeader);

            attributes.put("userId", userId);
        }
        return true; // 핸드쉐이크 진행
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception ex) {
    	// 핸드쉐이크 후 처리
    }
}
