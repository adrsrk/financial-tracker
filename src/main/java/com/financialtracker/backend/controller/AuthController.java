package com.financialtracker.backend.controller;

import com.financialtracker.backend.entity.User;
import com.financialtracker.backend.model.AuthenticationRequestDTO;
import com.financialtracker.backend.model.AuthenticationResponseDTO;
import com.financialtracker.backend.model.RegisterRequestDTO;
import com.financialtracker.backend.repository.UserRepository;
import com.financialtracker.backend.service.JwtService;
import com.financialtracker.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {

        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO request) {
        AuthenticationResponseDTO response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDTO> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");
        String email = jwtService.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!refreshToken.equals(user.getRefreshToken()) || !jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtService.generateAccessToken(user.getEmail());

        return ResponseEntity.ok(new AuthenticationResponseDTO(newAccessToken, refreshToken));
    }
}
