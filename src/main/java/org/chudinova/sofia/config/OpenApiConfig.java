package org.chudinova.sofia.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@OpenAPIDefinition(
//        info = @Info(
//                title = "Task Management System API",
//                version = "1.0",
//                description = "API для системы управления задачами",
//                contact = @Contact(
//                        name = "Chudinova Sofia",
//                        email = "Chudinova169t@gmail.com"
//                )
//        ),
//        security = @SecurityRequirement(name = "JWT")
//)
//@SecurityScheme(
//        name = "JWT",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Task Management System API")
                        .version("1.0")
                        .description("Документация API для управления задачами"));
    }
}
