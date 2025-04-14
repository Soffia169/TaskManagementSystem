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
import org.chudinova.sofia.exceptions.InvalidRequestException;
import org.chudinova.sofia.exceptions.UnauthorizedException;
import org.chudinova.sofia.models.requests.AuthRequest;
import org.chudinova.sofia.models.requests.RegisterRequest;
import org.chudinova.sofia.models.responses.InvalidRequestExceptionResponse;
import org.chudinova.sofia.models.responses.JwtResponse;
import org.chudinova.sofia.models.responses.UnauthorizedExceptionResponse;
import org.chudinova.sofia.models.responses.UserInfo;
import org.chudinova.sofia.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления аутентификацией пользователей
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Регистрация и вход в систему")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    /**
     * Вход пользователя в систему
     * @param request DTO с данными для входа
     * @return Ответ с JWT-токеном
     * @throws InvalidRequestException если введены некорректные данные
     * @throws UnauthorizedException если введены неверные учётные данные
     */
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
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = InvalidRequestExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Неверные учётные данные",
                    content = @Content(schema = @Schema(implementation = UnauthorizedExceptionResponse.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@RequestBody @Valid AuthRequest request) {
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

    /**
     * Регистрация нового пользователя в системе
     * @param request DTO с данными для регистрации
     * @return DTO созданного пользователя
     * @throws InvalidRequestException если введены некорректные данные
     */
    @Operation(
            summary = "Регистрация пользователя",
            description = "Создание нового пользователя в системе"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешная регистрация",
                    content = @Content(schema = @Schema(implementation = UserInfo.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = InvalidRequestExceptionResponse.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserInfo> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("Email '" + request.getEmail() + "' is already taken");
        }
        if (userService.existsByUsername(request.getUsername())) {
            throw new InvalidRequestException("Username '" + request.getUsername() + "' is already taken");
        }
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }
}
