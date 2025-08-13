package com.financialtracker.backend.repository;

import com.financialtracker.backend.entity.Category;
import com.financialtracker.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
}
