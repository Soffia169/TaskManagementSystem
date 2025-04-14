package org.chudinova.sofia.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(
        name = "ErrorResponse",
        example = """
    {
        "statusCode": 401,
        "message": "Неверные учётные данные"
    }"""
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnauthorizedExceptionResponse {
    private int statusCode;
    private String message;
}
