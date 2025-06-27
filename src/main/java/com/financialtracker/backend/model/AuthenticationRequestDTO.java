package com.financialtracker.backend.model;

public record AuthenticationRequestDTO(
        String email,
        String password
) {
}
