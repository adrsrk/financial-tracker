package com.financialtracker.backend.service;

import com.financialtracker.backend.entity.Account;
import com.financialtracker.backend.entity.Category;
import com.financialtracker.backend.entity.Transaction;
import com.financialtracker.backend.entity.User;
import com.financialtracker.backend.entity.enums.TransactionType;
import com.financialtracker.backend.model.TransactionRequestDTO;
import com.financialtracker.backend.model.TransactionResponseDTO;
import com.financialtracker.backend.repository.AccountRepository;
import com.financialtracker.backend.repository.CategoryRepository;
import com.financialtracker.backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public void create(TransactionRequestDTO requestDTO, Authentication auth) {
        User user = (User) auth.getPrincipal();

        Account account = accountRepository.findById(requestDTO.accountId())
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Account not found or not yours"));

        Category category = categoryRepository.findById(requestDTO.categoryId())
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Category not found or not yours"));

        if (!category.getType().name().equals(requestDTO.type().name())) {
            throw new RuntimeException("Category type and transaction type mismatch");
        }

        if (requestDTO.type() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(requestDTO.amount()));
        } else {
            account.setBalance(account.getBalance().subtract(requestDTO.amount()));
        }

        accountRepository.save(account);
    }

    public List<TransactionResponseDTO> getAll(Authentication auth) {
        User user = (User) auth.getPrincipal();

        return transactionRepository.findByAccountUser(user)
                .stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getAccount().getName(),
                        t.getCategory().getName(),
                        t.getAmount(),
                        t.getType(),
                        t.getDescription(),
                        t.getDate()
                ))
                .toList();
    }

    public void delete(Long id, Authentication auth) {
        User user = (User) auth.getPrincipal();

        Transaction transaction = transactionRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found or not yours"));

        if (transaction.getType() == TransactionType.INCOME) {
            transaction.getAccount().setBalance(transaction.getAccount().getBalance().subtract(transaction.getAmount()));
        } else {
            transaction.getAccount().setBalance(transaction.getAccount().getBalance().add(transaction.getAmount()));
        }
        accountRepository.save(transaction.getAccount());

        transactionRepository.delete(transaction);
    }
}
