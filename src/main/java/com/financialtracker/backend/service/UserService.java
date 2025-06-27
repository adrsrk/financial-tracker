package com.financialtracker.backend.service;

import com.financialtracker.backend.entity.User;
import com.financialtracker.backend.model.AuthenticationRequestDTO;
import com.financialtracker.backend.model.AuthenticationResponseDTO;
import com.financialtracker.backend.model.RegisterRequestDTO;
import com.financialtracker.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Username is already in use");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());

        userRepository.save(user);
    }

    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(request.email());
        String refreshToken = jwtService.generateRefreshToken(request.email());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthenticationResponseDTO(accessToken, refreshToken);
    }
}
