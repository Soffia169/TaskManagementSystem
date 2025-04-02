package org.chudinova.sofia.errors;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppError {
    private int statusCode;
    private String message;
}
