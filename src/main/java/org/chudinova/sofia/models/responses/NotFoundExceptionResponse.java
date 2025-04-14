package org.chudinova.sofia.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(
        name = "NotFoundError",
        example = """
                {
                    "statusCode": 404,
                    "message": "User with id 1 not found"
                }"""
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotFoundExceptionResponse {
    private int statusCode;
    private String message;
}
