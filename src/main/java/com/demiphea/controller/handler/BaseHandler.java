package com.demiphea.controller.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.demiphea.exception.auth.TokenParserException;
import com.demiphea.model.api.ApiResponse;
import jakarta.validation.ValidationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BaseHandler
 *
 * @author demiphea
 * @since 17.0.9
 */
@RestControllerAdvice
public class BaseHandler {
    private String packageExceptionMessage(Exception e) {
        return
                "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + "]: " +
                        "(" + e.getClass().getSimpleName() + ") " + e.getMessage();
    }

    @ExceptionHandler(value = {TokenParserException.class, JWTVerificationException.class})
    public ApiResponse handleAuthException(Exception e) {
        return ApiResponse.unauthorized(
                packageExceptionMessage(e)
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ApiResponse handleValidationException(ValidationException e) {
        return ApiResponse.fail(
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String errorMsg = errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
        return ApiResponse.fail(
                errorMsg
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleOtherException(Exception e) {
        return ApiResponse.error(
                packageExceptionMessage(e)
        );
    }
}
