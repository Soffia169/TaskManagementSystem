package org.chudinova.sofia.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ErrorResponse",
        example = """
    {
        "statusCode": 400,
        "message": "Email 'user@example.com' is already taken"
    }"""
)
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
