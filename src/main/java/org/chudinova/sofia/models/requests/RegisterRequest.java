package org.chudinova.sofia.models.requests;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class RegisterRequest {
    @NonNull
    private String username;

    @NonNull
    private String email;

    @NonNull
    private String password;
}
