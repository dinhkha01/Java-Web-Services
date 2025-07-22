package com.example.ss8.controller;

import com.example.ss8.model.dto.ApiResponse;
import com.example.ss8.model.dto.DishDTO;
import com.example.ss8.model.entity.Dish;
import com.example.ss8.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
@Slf4j
public class DishController {

    private final DishService dishService;

    /**
     * Thêm món ăn mới
     * POST /dishes
     */
    @PostMapping()
    public ResponseEntity<ApiResponse<Dish>> createDish(
            @Valid @ModelAttribute DishDTO dishDTO) {

        log.info("Nhận request tạo món ăn mới: {}", dishDTO.getName());

        Dish createdDish = dishService.createDish(dishDTO);

        ApiResponse<Dish> response = ApiResponse.<Dish>builder()
                .status(true)
                .message("Tạo món ăn thành công")
                .data(createdDish)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cập nhật món ăn
     * PUT /dishes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Dish>> updateDish(
            @PathVariable Long id,
            @Valid @ModelAttribute DishDTO dishDTO) {

        log.info("Nhận request cập nhật món ăn ID: {}", id);

        Dish updatedDish = dishService.updateDish(id, dishDTO);

        ApiResponse<Dish> response = ApiResponse.<Dish>builder()
                .status(true)
                .message("Cập nhật món ăn thành công")
                .data(updatedDish)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Xóa món ăn
     * DELETE /dishes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDish(@PathVariable Long id) {

        log.info("Nhận request xóa món ăn ID: {}", id);

        dishService.deleteDish(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(true)
                .message("Xóa món ăn thành công")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Lấy danh sách tất cả món ăn
     * GET /dishes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Dish>>> getAllDishes(
           ) {

        log.info("Nhận request lấy danh sách món ăn");

        List<Dish> dishes = dishService.getAllDishes();
        ApiResponse<List<Dish>> response = ApiResponse.<List<Dish>>builder()
                .status(true)
                .message("Lấy danh sách món ăn thành công")
                .data(dishes)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Lấy chi tiết món ăn theo ID
     * GET /dishes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Dish>> getDishById(@PathVariable Long id) {

        log.info("Nhận request lấy món ăn ID: {}", id);

        Dish dish = dishService.getDishById(id);

        ApiResponse<Dish> response = ApiResponse.<Dish>builder()
                .status(true)
                .message("Lấy thông tin món ăn thành công")
                .data(dish)
                .build();

        return ResponseEntity.ok(response);
    }
}