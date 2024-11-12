package net.ionoff.service.error;


import net.ionoff.service.api.dto.ErrorMessageDto;

import java.math.BigDecimal;
import java.util.Collections;

public final class ErrorMessage {

    private ErrorMessage() {}

    public static ErrorMessageDto tokenExpired() {
        return ErrorMessageDto.builder()
                .code("tokenExpired")
                .message("JWT expired")
                .build();
    }

    public static ErrorMessageDto tokenInvalid() {
        return ErrorMessageDto.builder()
                .code("tokenInvalid")
                .message("JWT is invalid")
                .build();
    }

    public static ErrorMessageDto unauthorized() {
        return ErrorMessageDto
                .builder()
                .code("unauthorized")
                .message("Unauthorized request")
                .build();
    }

    public static ErrorMessageDto accessDenied(String message) {
        return ErrorMessageDto.builder()
                .code("accessDenied")
                .message(message == null ? "Access denied" : message)
                .build();
    }

    public static ErrorMessageDto notFound(String message) {
        return ErrorMessageDto.builder()
                .code("notFound")
                .message(message == null ? "Resource not found" : message)
                .build();
    }

    public static ErrorMessageDto internalServerError(String message) {
        return ErrorMessageDto.builder()
                .code("internalServerError")
                .message(message == null ? "null" : message)
                .build();
    }

    public static ErrorMessageDto invalidFileSize(String field, BigDecimal maxMb) {
        return ErrorMessageDto.builder()
                .code("invalidFileSize")
                .message(String.format("File must be not-empty and file size must not be greater than %sMB", maxMb))
                .fields(Collections.singletonList(field))
                .build();
    }
    /**
     *
     * @param springExceptionInternal Exception created by Spring
     * @return ServiceMessage with code depend on the exception
     */
    public static ErrorMessageDto springExceptionInternal(Exception springExceptionInternal) {
        String clazz = springExceptionInternal.getClass().getSimpleName();
        String firstChar = clazz.substring(0, 1).toLowerCase();
        String code = new StringBuilder(firstChar).append(clazz.substring(1)).toString();
        return ErrorMessageDto.builder()
                .code(code)
                .message(springExceptionInternal.getMessage())
                .build();
    }
}
