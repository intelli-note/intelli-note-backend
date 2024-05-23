package com.demiphea.auth;

import java.lang.annotation.*;

/**
 * AuthID
 * 被注解的字段获取用户ID
 * <p>
 * 仅当注解的字段类型为 {@link Long}、{@link String} 或 {@link com.demiphea.entity.User} 时才会被注入成功
 * </p>
 *
 * @author demiphea
 * @since 17.0.9
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthID {
}
