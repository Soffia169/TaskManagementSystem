package org.chudinova.sofia.services;

import lombok.RequiredArgsConstructor;
import org.chudinova.sofia.entities.User;
import org.chudinova.sofia.exceptions.RoleNotFoundException;
import org.chudinova.sofia.models.requests.RegisterRequest;
import org.chudinova.sofia.repositories.RoleRepository;
import org.chudinova.sofia.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public void createUser(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RoleNotFoundException("Role with name 'ROLE_ADMIN' not found")))
                .build();
        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
