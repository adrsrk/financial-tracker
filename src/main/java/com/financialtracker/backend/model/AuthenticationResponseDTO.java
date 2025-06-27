package com.financialtracker.backend.model;

public record AuthenticationResponseDTO(
        String accessToken,
        String refreshToken
) {
}
