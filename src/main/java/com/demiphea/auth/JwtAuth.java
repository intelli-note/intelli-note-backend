package com.demiphea.auth;

import cn.hutool.core.map.MapBuilder;
import com.demiphea.entity.User;
import com.demiphea.utils.jwt.JwtUtils;

/**
 * JwtAuth
 *
 * @author demiphea
 * @since 17.0.9
 */
public class JwtAuth {
    /**
     * 获取用户token令牌
     *
     * @param user 用户实体类
     * @return {@link String} token令牌
     * @author demiphea
     */
    public static String create(User user) {
        return JwtUtils.createDefault(
                MapBuilder.<String, Object>create()
                        .put("id", user.getId())
                        .build()
        );
    }

    /**
     * 获取用户token令牌
     *
     * @param id 用户ID
     * @return {@link String} token令牌
     * @author demiphea
     */
    public static String create(Long id) {
        return JwtUtils.createDefault(
                MapBuilder.<String, Object>create()
                        .put("id", id)
                        .build()
        );
    }

    /**
     * 校验用户令牌并返回用户ID
     *
     * @param token token令牌
     * @return {@link Long} 用户ID
     * @author demiphea
     */
    public static Long verify(String token) {
        return JwtUtils.verifyDefault(token).getClaim("id").asLong();
    }
}
