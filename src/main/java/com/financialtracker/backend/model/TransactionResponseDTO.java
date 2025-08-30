package com.financialtracker.backend.model;

import com.financialtracker.backend.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        String AccountName,
        String categoryName,
        BigDecimal amount,
        TransactionType type,
        String description,
        LocalDateTime date
) {
}
