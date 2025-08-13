package com.financialtracker.backend.controller;

import com.financialtracker.backend.model.CategoryRequestDTO;
import com.financialtracker.backend.model.CategoryResponseDTO;
import com.financialtracker.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll(Authentication auth) {
        return ResponseEntity.ok(categoryService.getAll(auth));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CategoryRequestDTO categoryRequestDTO, Authentication auth) {
        categoryService.create(categoryRequestDTO, auth);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody CategoryRequestDTO categoryRequestDTO,
                                       Authentication auth) {
        categoryService.update(id, categoryRequestDTO, auth);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        categoryService.delete(id, auth);
        return ResponseEntity.ok().build();
    }
}
