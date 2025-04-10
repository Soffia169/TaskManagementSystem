package org.chudinova.sofia.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ErrorResponse",
        example = """
    {
        "statusCode": 401,
        "message": "Неверные учётные данные"
    }"""
)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
