package com.demiphea.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.demiphea.utils.aliyun.ai.AliYunQWenChatModel;
import com.demiphea.utils.json.JsonObjectBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.hc.core5.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * AIContinuedWebsocket
 *
 * @author demiphea
 * @since 17.0.9
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AIContinuedWebsocket extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        AliYunQWenChatModel.call(
                AliYunQWenChatModel.CONTINUED_PROMPT,
                JsonObjectBuilder.create()
                        .put("content", message.getPayload())
                        .build().toString(),
                new EventSourceListener() {
                    @Override
                    public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                        if ("[DONE]".equals(data)) {
                            try {
                                session.sendMessage(new TextMessage(JsonObjectBuilder.create()
                                        .put("code", HttpStatus.SC_OK)
                                        .put("message", "Success!")
                                        .build().toString()
                                ));
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                            return;
                        }
                        String content = JSONObject.parse(data)
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("delta")
                                .getString("content");
                        if (content == null) {
                            return;
                        }
                        try {
                            session.sendMessage(new TextMessage(JsonObjectBuilder.create()
                                    .put("code", HttpStatus.SC_CONTINUE)
                                    .put("data", content)
                                    .build().toString()
                            ));
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                        try {
                            session.sendMessage(new TextMessage(JsonObjectBuilder.create()
                                    .put("code", HttpStatus.SC_INTERNAL_SERVER_ERROR)
                                    .put("message", "服务端响应错误，请联系系统管理员")
                                    .build().toString()
                            ));
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
        );
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error(exception.getMessage());
    }
}
