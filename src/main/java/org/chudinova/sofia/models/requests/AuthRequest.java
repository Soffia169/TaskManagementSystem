package org.chudinova.sofia.models.requests;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class AuthRequest {
    @NonNull
    private String email;

    @NonNull
    private String password;
}
