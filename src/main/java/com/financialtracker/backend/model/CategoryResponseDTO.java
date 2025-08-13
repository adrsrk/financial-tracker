package com.financialtracker.backend.model;

import com.financialtracker.backend.entity.enums.CategoryType;

public record CategoryResponseDTO(
        Long id,
        String name,
        CategoryType type) {
}
