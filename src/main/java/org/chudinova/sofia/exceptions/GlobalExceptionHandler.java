package org.chudinova.sofia.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.chudinova.sofia.errors.AppError;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> catchUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchStatusNotFoundException(StatusNotFoundException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchPriorityNotFoundException(PriorityNotFoundException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchInvalidRequestException(InvalidRequestException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchTaskNotFoundException(TaskNotFoundException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchRoleNotFoundException(RoleNotFoundException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppError> catchMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Validation failed"), HttpStatus.BAD_REQUEST);
    }

}
