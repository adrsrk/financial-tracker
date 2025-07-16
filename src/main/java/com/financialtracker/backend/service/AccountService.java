package com.financialtracker.backend.service;

import com.financialtracker.backend.entity.Account;
import com.financialtracker.backend.entity.User;
import com.financialtracker.backend.model.AccountRequestDTO;
import com.financialtracker.backend.model.AccountResponseDTO;
import com.financialtracker.backend.repository.AccountRepository;
import com.financialtracker.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public void create(AccountRequestDTO requestDTO, Authentication auth) {

        User user = (User) auth.getPrincipal();

        Account account = Account.builder()
                .user(user)
                .name(requestDTO.getName())
                .currency(requestDTO.getCurrency() != null ? requestDTO.getCurrency() : "KZT")
                .balance(null)
                .build();

        accountRepository.save(account);
    }

    public List<AccountResponseDTO> getAll(Authentication auth) {

        User user = (User) auth.getPrincipal();

        return accountRepository.findByUser(user).stream()
                .map(account -> AccountResponseDTO.builder()
                        .id(account.getId())
                        .name(account.getName())
                        .currency(account.getCurrency())
                        .balance(account.getBalance())
                        .createdAt(account.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }
}
