package com.financialtracker.backend.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponseDTO {
    private Long id;
    private String name;
    private String currency;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
