package org.chudinova.sofia.services;

import lombok.RequiredArgsConstructor;
import org.chudinova.sofia.entities.Task;
import org.chudinova.sofia.entities.User;
import org.chudinova.sofia.exceptions.InvalidRequestException;
import org.chudinova.sofia.exceptions.RoleNotFoundException;
import org.chudinova.sofia.models.requests.RegisterRequest;
import org.chudinova.sofia.models.responses.TaskResponse;
import org.chudinova.sofia.models.responses.UserInfo;
import org.chudinova.sofia.repositories.RoleRepository;
import org.chudinova.sofia.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы с пользователями
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Создание пользователя
     * @param request DTO с данными пользователя
     * @return DTO созданного пользователя
     */
    @Transactional
    public UserInfo createUser(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findByName("ROLE_ADMIN").orElseThrow())
                .build();
        return convertToDto(userRepository.save(user));
    }

    /**
     * Проверка на существование email
     * @param email email пользователя
     * @return true/false
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Проверка на существование username
     * @param username username пользователя
     * @return true/false
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Преобразование в DTO
     * @param user сущность пользователя
     * @return DTO пользователя
     */
    private UserInfo convertToDto(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .name(user.getUsername())
                .build();
    }
}
