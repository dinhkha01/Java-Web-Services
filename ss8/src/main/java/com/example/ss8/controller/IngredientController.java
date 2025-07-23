package com.example.ss8.controller;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.exception.NotFoundException;
import com.example.ss8.model.dto.request.IngredientDTO;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.Ingredient;
import com.example.ss8.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    /**
     * Nhập nguyên liệu mới
     */
    @PostMapping
    public ResponseEntity<DataResponse<Ingredient>> createIngredient(
            @Valid @ModelAttribute IngredientDTO ingredientDTO) throws BadRequestException {
        return new ResponseEntity<>(ingredientService.createIngredient(ingredientDTO), HttpStatus.CREATED);
    }

    /**
     * Sửa nguyên liệu
     */
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<Ingredient>> updateIngredient(
            @PathVariable Long id,
            @Valid @ModelAttribute IngredientDTO ingredientDTO) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(ingredientService.updateIngredient(id, ingredientDTO), HttpStatus.OK);
    }

    /**
     * Xóa nguyên liệu
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<String>> deleteIngredient(@PathVariable Long id) throws NotFoundException {
        ingredientService.deleteIngredient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Lấy danh sách nguyên liệu
     */
    @GetMapping
    public ResponseEntity<DataResponse<List<Ingredient>>> getAllIngredients() {
        return new ResponseEntity<>(ingredientService.getAllIngredients(), HttpStatus.OK);
    }

    /**
     * Lấy nguyên liệu theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(ingredientService.getIngredientById(id), HttpStatus.OK);
    }
}