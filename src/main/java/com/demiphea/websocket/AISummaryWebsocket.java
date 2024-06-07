package com.demiphea.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.demiphea.dao.NoteDao;
import com.demiphea.entity.Note;
import com.demiphea.service.inf.PermissionService;
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
 * AISummaryWebsocket
 *
 * @author demiphea
 * @since 17.0.9
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AISummaryWebsocket extends TextWebSocketHandler {
    private final NoteDao noteDao;
    private final PermissionService permissionService;

    /**
     * 接收消息
     *
     * @param session 会话
     * @param message 消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Long userId = (Long) session.getAttributes().get("id");
        long noteId;
        try {
            noteId = Long.parseLong(message.getPayload());
        } catch (Exception e) {
            session.sendMessage(
                    new TextMessage(JsonObjectBuilder.create()
                            .put("code", HttpStatus.SC_BAD_REQUEST)
                            .put("message", "传入参数解析错误")
                            .build().toString())
            );
            return;
        }
        Note note = noteDao.selectById(noteId);
        if (note == null) {
            session.sendMessage(
                    new TextMessage(JsonObjectBuilder.create()
                            .put("code", HttpStatus.SC_NOT_FOUND)
                            .put("message", "笔记不存在或已删除")
                            .build().toString()
                    ));
            return;
        }
        if (!permissionService.checkNoteReadPermission(userId, note)) {
            session.sendMessage(
                    new TextMessage(JsonObjectBuilder.create()
                            .put("code", HttpStatus.SC_FORBIDDEN)
                            .put("message", "没有权限访问")
                            .build().toString()
                    ));
            return;
        }
        AliYunQWenChatModel.call(
                AliYunQWenChatModel.SUMMARY_PROMPT,
                JsonObjectBuilder.create()
                        .put("title", note.getTitle())
                        .put("content", note.getContent())
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
                });


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
    }
}
