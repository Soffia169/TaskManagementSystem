package org.chudinova.sofia.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(
        name = "SecurityErrorResponse",
        example = """
    {
        "statusCode": 403,
        "message": "Отказано в доступе"
    }"""
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityExceptionResponse {
    private int statusCode;
    private String message;
}
