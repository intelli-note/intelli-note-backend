package com.demiphea.model.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * ApiResponse
 *
 * @author demiphea
 * @since 17.0.9
 */
public class ApiResponse extends ResponseEntity<Result> {
    public ApiResponse(HttpStatusCode status) {
        super(status);
    }

    public ApiResponse(Result body, HttpStatusCode status) {
        super(body, status);
    }

    public ApiResponse(MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(headers, status);
    }

    public ApiResponse(Result body, MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(body, headers, status);
    }

    public ApiResponse(Result body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public static ApiResponse success(Result body) {
        return new ApiResponse(body, HttpStatus.OK);
    }

    public static ApiResponse success(Integer code, String msg, Object data) {
        return success(new Result(code, msg, data));
    }

    public static ApiResponse success(Object data) {
        return success(new Result(200, "Success", data));
    }

    public static ApiResponse success() {
        return success(new Result(200, "Success", null));
    }

    public static ApiResponse fail(Result body) {
        return new ApiResponse(body, HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse fail(Integer code, String msg, Object data) {
        return fail(new Result(code, msg, data));
    }

    public static ApiResponse fail(String msg, Object data) {
        return fail(new Result(null, msg, data));
    }

    public static ApiResponse fail(String msg) {
        return fail(new Result(null, msg, null));
    }

    public static ApiResponse unauthorized(Result body) {
        return new ApiResponse(body, HttpStatus.UNAUTHORIZED);
    }

    public static ApiResponse unauthorized(Integer code, String msg, Object data) {
        return unauthorized(new Result(code, msg, data));
    }

    public static ApiResponse unauthorized(String msg, Object data) {
        return unauthorized(new Result(null, msg, data));
    }

    public static ApiResponse error(Result body) {
        return new ApiResponse(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ApiResponse error(Integer code, String msg, Object data) {
        return error(new Result(code, msg, data));
    }

    public static ApiResponse error(String msg, Object data) {
        return error(new Result(null, msg, data));
    }

    public static ApiResponse error(String msg) {
        return error(new Result(null, msg, null));
    }

    public static ApiResponse unauthorized(String msg) {
        return unauthorized(new Result(null, msg, null));
    }

    public static ApiResponse unauthorized() {
        return unauthorized(new Result(null, "用户未登录或登录过期", null));
    }
}
