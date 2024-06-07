package com.demiphea.utils.openai;

import com.demiphea.utils.json.JsonObjectBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSourceListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OpenAI
 *
 * @author demiphea
 * @since 17.0.9
 */
public class OpenAI {
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private final Request request;

    private OpenAI(Request request) {
        this.request = request;
    }

    public void streamCall(EventSourceListener listener) {
        RealEventSource eventSource = new RealEventSource(request, listener);
        eventSource.connect(client);
    }

    public static class Builder {
        private final Request.Builder requestBuilder;
        private final JsonObjectBuilder bodyBuilder;

        private final List<Message> messages;

        /**
         * OpenAI 构造器
         *
         * @param url    url
         * @param apikey apikey
         */
        public Builder(String url, String apikey) {
            this.requestBuilder = new Request.Builder();
            this.requestBuilder.url(url);
            this.requestBuilder.addHeader("Authorization", "Bearer " + apikey);

            this.bodyBuilder = JsonObjectBuilder.create();

            this.messages = new ArrayList<>();
        }

        /**
         * 用户使用model参数指明对应的模型
         */
        public Builder model(Model model) {
            this.bodyBuilder.put("model", model.getName());
            return this;
        }

        /**
         * 用户与模型对话上下文操作-添加对话
         */
        public Builder putMessage(Message message) {
            this.messages.add(message);
            return this;
        }

        /**
         * 用户与模型对话上下文操作-添加对话
         */
        public Builder putMessages(Message... messages) {
            this.messages.addAll(Arrays.asList(messages));
            return this;
        }

        /**
         * 用户与模型对话上下文操作-添加对话
         */
        public Builder putMessages(List<Message> messages) {
            this.messages.addAll(messages);
            return this;
        }

        /**
         * 用户与模型对话上下文操作-清空对话
         */
        public Builder clearMessages() {
            this.messages.clear();
            return this;
        }

        /**
         * 生成过程中的核采样方法概率阈值，例如，取值为0.8时，仅保留概率加起来大于等于0.8的最可能token的最小集合作为候选集。取值范围为（0,1.0)，取值越大，生成的随机性越高；取值越低，生成的确定性越高。
         */
        public Builder topP(float topP) {
            this.bodyBuilder.put("top_p", topP);
            return this;
        }

        /**
         * 用于控制模型回复的随机性和多样性。具体来说，temperature值控制了生成文本时对每个候选词的概率分布进行平滑的程度。较高的temperature值会降低概率分布的峰值，使得更多的低概率词被选择，生成结果更加多样化；而较低的temperature值则会增强概率分布的峰值，使得高概率词更容易被选择，生成结果更加确定。
         * <br>
         * 取值范围： [0, 2)，不建议取值为0，无意义。
         */
        public Builder temperature(float temperature) {
            this.bodyBuilder.put("temperature", temperature);
            return this;
        }

        /**
         * 用户控制模型生成时整个序列中的重复度。提高presence_penalty时可以降低模型生成的重复度，取值范围[-2.0, 2.0]。
         */
        public Builder presencePenalty(float presencePenalty) {
            this.bodyBuilder.put("presence_penalty", presencePenalty);
            return this;
        }

        /**
         * 指定模型可生成的最大token个数。根据模型不同有不同的上限限制，一般不超过2000。
         */
        public Builder maxTokens(int maxTokens) {
            this.bodyBuilder.put("max_tokens", maxTokens);
            return this;
        }

        /**
         * 生成时使用的随机数种子，用于控制模型生成内容的随机性。seed支持无符号64位整数。
         */
        public Builder seed(int seed) {
            this.bodyBuilder.put("seed", seed);
            return this;
        }

        /**
         * 用于控制是否使用流式输出。当以stream模式输出结果时，接口返回结果为generator，需要通过迭代获取结果，每次输出为当前生成的增量序列。
         */
        public Builder stream(boolean stream) {
            this.bodyBuilder.put("stream", stream);
            return this;
        }

        /**
         * stop参数用于实现内容生成过程的精确控制，在模型生成的内容即将包含指定的字符串或token_id时自动停止。stop可以为string类型或array类型。
         * <br>
         * <li>string类型</li>
         * 当模型将要生成指定的stop词语时停止。
         * <br>
         * 例如将stop指定为"你好"，则模型将要生成“你好”时停止。
         * <br>
         * <li>array类型</li>
         * array中的元素可以为token_id或者字符串，或者元素为token_id的array。当模型将要生成的token或其对应的token_id在stop中时，模型生成将会停止。以下为stop为array时的示例（tokenizer对应模型为qwen-turbo）：
         * <br>
         * 1.元素为token_id：
         * <br>
         * token_id为108386和104307分别对应token为“你好”和“天气”，设定stop为[108386,104307]，则模型将要生成“你好”或者“天气”时停止。
         * <br>
         * 2.元素为字符串：
         * <br>
         * 设定stop为["你好","天气"]，则模型将要生成“你好”或者“天气”时停止。
         * <br>
         * 3.元素为array：
         * <br>
         * token_id为108386和103924分别对应token为“你好”和“啊”，token_id为35946和101243分别对应token为“我”和“很好”。设定stop为[[108386, 103924],[35946, 101243]]，则模型将要生成“你好啊”或者“我很好”时停止。
         */
        public Builder stop(Object stop) {
            this.bodyBuilder.put("stop", stop);
            return this;
        }

        /**
         * 未实现的方法
         */
        private Builder tools(Object tools) {
            this.bodyBuilder.put("tools", tools);
            return this;
        }

        /**
         * 该参数用于配置在流式输出时是否展示使用的token数目。只有当stream为True的时候该参数才会激活生效。若您需要统计流式输出模式下的token数目，可将该参数配置为stream_options={"include_usage":True}。
         */
        public Builder streamOptions(Object option) {
            this.bodyBuilder.put("stream_options", option);
            return this;
        }

        /**
         * 用于控制模型在生成文本时是否使用互联网搜索结果进行参考。取值如下：
         * <li>
         * True：启用互联网搜索，模型会将搜索结果作为文本生成过程中的参考信息，但模型会基于其内部逻辑判断是否使用互联网搜索结果。
         * </li>
         * <li>
         * False（默认）：关闭互联网搜索。
         * </li>
         * <br>
         * 配置方式为：extra_body={"enable_search":True}
         */
        public Builder enableSearch(boolean enableSearch) {
            this.bodyBuilder.put("enable_search", enableSearch);
            return this;
        }

        /**
         * 自定义请求参数（未实现的方法）
         */
        private Builder putCustomParam(String key, Object value) {
            this.bodyBuilder.put(key, value);
            return this;
        }

        public OpenAI build() {
            this.bodyBuilder.put(!messages.isEmpty(), "messages", messages);

            return new OpenAI(
                    this.requestBuilder
                            .post(RequestBody.create(bodyBuilder.build().toString(), MediaType.parse("application/json")))
                            .build()
            );
        }
    }
}
