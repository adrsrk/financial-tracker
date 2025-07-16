package com.financialtracker.backend.model;

import lombok.Data;

@Data
public class AccountRequestDTO {
    private String name;
    private String currency;
}
