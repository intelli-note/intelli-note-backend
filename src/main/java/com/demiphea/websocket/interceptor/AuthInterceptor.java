package com.demiphea.websocket.interceptor;

import com.demiphea.auth.JwtAuth;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AuthInterceptor
 *
 * @author demiphea
 * @since 17.0.9
 */
@Component
public class AuthInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String jwt = request.getHeaders().getFirst("Authorization");
        if (jwt == null) {
            return false;
        }
        Matcher matcher = Pattern.compile("^Bearer (?<token>.+)$").matcher(jwt.trim());
        if (!matcher.matches()) {
            return false;
        }
        String token = matcher.group("token");
        // token校验
        Long id;
        try {
            id = JwtAuth.verify(token);
        } catch (Exception e) {
            return false;
        }
        attributes.put("id", id);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
