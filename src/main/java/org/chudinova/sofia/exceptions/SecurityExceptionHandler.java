package org.chudinova.sofia.exceptions;

import org.chudinova.sofia.errors.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class SecurityExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public AppError handleAccessDeniedException(
            AccessDeniedException ex,
            WebRequest request
    ) {
        return AppError.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message("Доступ запрещён: недостаточно прав")
                .build();
    }
}
