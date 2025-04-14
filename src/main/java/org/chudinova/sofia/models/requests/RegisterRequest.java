package org.chudinova.sofia.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Schema(
        description = "Данные для регистрации",
        example = """
                {
                    "username": "user",
                    "email": "user@example.com",
                    "password": "P@ssw0rd"
                }
                """
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;
}
