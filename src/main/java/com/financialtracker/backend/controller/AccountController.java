package com.financialtracker.backend.controller;

import com.financialtracker.backend.model.AccountRequestDTO;
import com.financialtracker.backend.model.AccountResponseDTO;
import com.financialtracker.backend.service.AccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody AccountRequestDTO accountRequestDTO, Authentication authentication) {
        accountService.create(accountRequestDTO, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAll(Authentication authentication) {
        return ResponseEntity.ok(accountService.getAll(authentication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody AccountRequestDTO accountRequestDTO,
            Authentication authentication) {

        accountService.update(id, accountRequestDTO, authentication);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        accountService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
