package com.financialtracker.backend.service;

import com.financialtracker.backend.entity.Category;
import com.financialtracker.backend.entity.User;
import com.financialtracker.backend.model.CategoryRequestDTO;
import com.financialtracker.backend.model.CategoryResponseDTO;
import com.financialtracker.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void create(CategoryRequestDTO categoryRequestDTO, Authentication auth) {

        User user = (User) auth.getPrincipal();

        Category category = Category.builder()
                .name(categoryRequestDTO.name())
                .type(categoryRequestDTO.type())
                .user(user)
                .build();

        categoryRepository.save(category);
    }

    public List<CategoryResponseDTO> getAll(Authentication auth) {

        User user = (User) auth.getPrincipal();

        return categoryRepository.findByUser(user)
                .stream()
                .map(c -> new CategoryResponseDTO(c.getId(), c.getName(), c.getType()))
                .toList();
    }

    public void update(Long categoryId, CategoryRequestDTO categoryRequestDTO, Authentication auth) {
        User user = (User) auth.getPrincipal();

        Category category = categoryRepository.findById(categoryId)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Category not found or not yours"));

        category.setName(categoryRequestDTO.name());
        category.setType(categoryRequestDTO.type());
        categoryRepository.save(category);
    }

    public void delete(Long categoryId, Authentication auth) {

        User user = (User) auth.getPrincipal();

        Category category = categoryRepository.findById(categoryId)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Category not found or not yours"));

        categoryRepository.delete(category);
    }
}
