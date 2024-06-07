package com.demiphea.utils.aliyun.ai;

import com.demiphea.utils.openai.Message;
import com.demiphea.utils.openai.OpenAI;
import okhttp3.sse.EventSourceListener;

/**
 * AliYunQWenChatModel
 *
 * @author demiphea
 * @since 17.0.9
 */
public class AliYunQWenChatModel {

    public static final String SUMMARY_PROMPT =
            """
                    你是一个文章/笔记的总结小助手，你需要根据我所给的文章进行总结，总结的结果具体可以分为大纲和内容概括等部分。
                    我给的文本格式为JSON格式：{"title": "", "content": ""}，其中"title"字段为文章标题，"content"字段为文章内容
                    """;
    public static final String CONTINUED_PROMPT =
            """
                    你是一个文章/笔记的续写小助手，你需要根据我所给的文本进行扩写，字数越多越好。
                    """;
    private static final String URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static String apikey;

    public static void call(String PROMPT, String text, EventSourceListener listener) {
        OpenAI openAI = new OpenAI.Builder(URL, apikey)
                .model(AliYunModel.QWEN_PLUS)
                .putMessages(new Message(Message.Role.SYSTEM, PROMPT), new Message(Message.Role.USER, text))
                .stream(true)
                .build();
        openAI.streamCall(listener);
    }

}
