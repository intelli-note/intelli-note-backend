package com.demiphea.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

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


    @Around("@annotation(auth)")
    public Object doAuthentication(ProceedingJoinPoint method, Auth auth) {
        return null;
    }
}
