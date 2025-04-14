package org.chudinova.sofia.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(
        description = "Информация о пользователе",
        example = """
                {
                    "id": 1,
                    "name": "User_1"
                }
                """
)
@Data
@Builder
public class UserInfo {
    private Long id;
    private String name;
}
