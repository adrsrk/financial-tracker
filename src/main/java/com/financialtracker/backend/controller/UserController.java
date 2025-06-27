package com.financialtracker.backend.controller;

import com.financialtracker.backend.entity.User;
import com.financialtracker.backend.repository.UserRepository;
import com.financialtracker.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<String> me(Authentication authentication) {
        return ResponseEntity.ok("Authenticated as: " + authentication.getName());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setRefreshToken(null);
        userRepository.save(user);
        return ResponseEntity.ok("Logged out successfully");
    }

}
