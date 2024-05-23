package com.demiphea.auth;

import java.lang.annotation.*;

/**
 * Auth
 * 用户权限校验
 *
 * @author demiphea
 * @since 17.0.9
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
}
