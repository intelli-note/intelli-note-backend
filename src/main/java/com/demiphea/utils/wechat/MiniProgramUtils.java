package com.demiphea.utils.wechat;

import cn.hutool.core.map.MapBuilder;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.demiphea.utils.network.HttpUtils;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * MiniProgramUtils
 *
 * @author demiphea
 * @since 17.0.9
 */
public class MiniProgramUtils {
    private static String appId;
    private static String appSecret;


    /**
     * 小程序登录
     *
     * <p>
     * 详情见 <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html">接口说明</a>
     * </p>
     *
     * @param code 登录时获取的 code，可通过wx.login获取
     * @return {@link JSONObject}
     * @author demiphea
     */
    public static JSONObject login(String code) throws URISyntaxException, IOException, ParseException {
        String response = HttpUtils.simpleGet(
                Api.LOGIN.url,
                MapBuilder.<String, String>create()
                        .put("appid", appId)
                        .put("secret", appSecret)
                        .put("js_code", code)
                        .put("grant_type", "authorization_code")
                        .build()
        );
        return JSON.parseObject(response);
    }

    private enum Api {
        LOGIN("https://api.weixin.qq.com/sns/jscode2session");
        public final String url;

        Api(String url) {
            this.url = url;
        }
    }


}
