package com.financialtracker.backend.model;

public record RegisterRequestDTO(
        String email,
        String password,
        String fullName) {
}
