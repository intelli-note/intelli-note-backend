package com.demiphea.config;

import com.demiphea.websocket.AISummaryWebsocket;
import com.demiphea.websocket.NoticeWebsocket;
import com.demiphea.websocket.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocketConfig
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final AuthInterceptor authInterceptor;

    private final NoticeWebsocket noticeWebsocket;
    private final AISummaryWebsocket aiSummaryWebsocket;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(noticeWebsocket, "/notice")
                .addInterceptors(authInterceptor);

        registry.addHandler(aiSummaryWebsocket, "/ai/summary")
                .addInterceptors(authInterceptor);
    }
}
