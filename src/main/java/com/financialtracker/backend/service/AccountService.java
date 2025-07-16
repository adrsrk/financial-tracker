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

    public void update(Long accountId, AccountRequestDTO requestDTO, Authentication auth) {

        User user = (User) auth.getPrincipal();

        Account account = accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Account not found or not yours"));

        account.setName(requestDTO.getName());
        if (requestDTO.getCurrency() != null) {
            account.setCurrency(requestDTO.getCurrency());
        }

        accountRepository.save(account);
    }

    public void delete(Long accountId, Authentication auth) {

        User user = (User) auth.getPrincipal();

        Account account = accountRepository.findById(accountId)
                .filter(a -> a.getUser().getId().equals(accountId))
                .orElseThrow(() -> new RuntimeException("Account not found or not yours"));

        accountRepository.delete(account);
    }
}
