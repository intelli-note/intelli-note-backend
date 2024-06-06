package com.demiphea.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NoticeWebsocket
 *
 * @author demiphea
 * @since 17.0.9
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class NoticeWebsocket extends TextWebSocketHandler {
    public final static Map<Long, WebSocketSession> POOL = new ConcurrentHashMap<>();

    /**
     * 建立连接
     *
     * @param session 会话
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("id");
        POOL.put(userId, session);
    }

    /**
     * 接收消息
     *
     * @param session 会话
     * @param message 消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

    }

    /**
     * 处理错误
     *
     * @param session   会话
     * @param exception 异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error(exception.getMessage());
        Long userId = (Long) session.getAttributes().get("id");
        POOL.remove(userId);
    }

    /**
     * 连接关闭
     *
     * @param session 会话
     * @param status  关闭状态
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("id");
        POOL.remove(userId);
    }
}
