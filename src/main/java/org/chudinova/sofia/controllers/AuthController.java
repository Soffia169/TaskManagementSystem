package org.chudinova.sofia.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chudinova.sofia.config.JwtUtils;
import org.chudinova.sofia.errors.AppError;
import org.chudinova.sofia.exceptions.InvalidRequestException;
import org.chudinova.sofia.exceptions.UnauthorizedException;
import org.chudinova.sofia.models.requests.AuthRequest;
import org.chudinova.sofia.models.requests.RegisterRequest;
import org.chudinova.sofia.models.responses.JwtResponse;
import org.chudinova.sofia.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Регистрация и вход в систему")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Вход в систему"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход в систему",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = UnauthorizedException.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (
            @RequestBody @Valid
            @Schema(
                    description = "Данные для входа",
                    example = """
                        {
                            "email": "user@example.com",
                            "password": "P@ssw0rd"
                        }
                        """
            )
            AuthRequest request
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Неверные учётные данные");
        }
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создание нового пользователя в системе"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная регистрация"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = InvalidRequestException.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody
            @Schema(description = "Данные для регистрации", example = "{\"username\": \"user\", \"email\": " +
                    "\"user@example.com\", \"password\": \"P@ssw0rd\"}")
            RegisterRequest request
    ) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username cannot be null");
        }
        if (request.getPassword() == null) {
            throw new InvalidRequestException("Password cannot be null");
        }
        if (request.getEmail() == null) {
            throw new InvalidRequestException("Email cannot be null");
        }
        if (userService.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("Email '" + request.getEmail() + "' is already taken");
        }
        if (userService.existsByUsername(request.getUsername())) {
            throw new InvalidRequestException("Username '" + request.getUsername() + "' is already taken");
        }
        userService.createUser(request);
        return ResponseEntity.ok().build();
    }
}
