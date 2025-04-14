package org.chudinova.sofia.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "JWT-токен",
        example = """
                {
                    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVc2VyMUBleGFtcGxlLmNvbSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NDQyODk0NzgsImV4cCI6MTc0NDM3NTg3OH0.uUg9v-cbIcwyX74B58AfgMDZK9bxowwMzfC7Hp2BVUlTa-C180Ia3E_0A-TBPL0GdPKKvmu6NsVQ2ffZ7h-R8g"
                }
                """
)
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JwtResponse {
    private String token;
}
