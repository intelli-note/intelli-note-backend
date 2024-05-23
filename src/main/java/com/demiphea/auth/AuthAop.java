package com.demiphea.auth;

import com.demiphea.entity.User;
import com.demiphea.exception.auth.TokenParserException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AuthAop
 *
 * @author demiphea
 * @since 17.0.9
 */
@Component
@Aspect
@RequiredArgsConstructor
public class AuthAop {
    private final HttpServletRequest request;

    @Around("@annotation(com.demiphea.auth.Auth)")
    public Object doAuthentication(ProceedingJoinPoint method) throws Throwable {
        // 从请求头中解析出token
        String jwt = request.getHeader("Authorization");
        if (jwt == null) {
            throw new TokenParserException("请求头中未携带令牌");
        }
        Matcher matcher = Pattern.compile("^Bearer (?<token>.+)$").matcher(jwt.trim());
        if (!matcher.matches()) {
            throw new TokenParserException("认证失败！请求头错误");
        }
        String token = matcher.group("token");
        // token校验
        Long id = JwtAuth.verify(token);
        // 注入ID
        Method invokeMethod = ((MethodSignature) method.getSignature()).getMethod();

        Object[] args = method.getArgs();
        Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
        Annotation[][] parameterAnnotations = invokeMethod.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                if (parameterAnnotations[i][j].annotationType().equals(AuthID.class)) {
                    Class<?> type = parameterTypes[i];
                    if (long.class.equals(type) || Long.class.equals(type)) {
                        args[i] = id;
                    } else if (String.class.equals(type)) {
                        args[i] = id.toString();
                    } else if (User.class.equals(type)) {
                        ((User) args[i]).setId(id);
                    }
                    break;
                }
            }
        }
        return method.proceed(args);
    }
}
