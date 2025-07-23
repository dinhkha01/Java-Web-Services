package com.example.ss8.repository;

import com.example.ss8.model.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Kiểm tra tên nguyên liệu đã tồn tại (không phân biệt hoa thường)
     */
    boolean existsByNameIgnoreCase(String name);
}