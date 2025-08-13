package com.financialtracker.backend.model;

import com.financialtracker.backend.entity.enums.CategoryType;

public record CategoryRequestDTO(
        String name,
        CategoryType type
) {
}
