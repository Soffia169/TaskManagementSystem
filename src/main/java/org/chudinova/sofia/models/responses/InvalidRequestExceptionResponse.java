package org.chudinova.sofia.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(
        name = "InvalidRequest",
        example = """
    {
        "statusCode": 400,
        "message": "Validation failed"
    }"""
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidRequestExceptionResponse {
    private int statusCode;
    private String message;
}
