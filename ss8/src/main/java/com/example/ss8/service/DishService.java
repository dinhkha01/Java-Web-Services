package com.example.ss8.service;


import com.example.ss8.exception.BadRequestException;
import com.example.ss8.model.dto.DishDTO;
import com.example.ss8.model.entity.Dish;
import com.example.ss8.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DishService {

    private final DishRepository dishRepository;
    private final CloudinaryService cloudinaryService;

    /**
     * Thêm món ăn mới
     */
    public Dish createDish(DishDTO dishDTO) {
        try {
            // Upload ảnh lên Cloudinary nếu có
            String imageUrl = null;
            if (dishDTO.getImage() != null && !dishDTO.getImage().isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(dishDTO.getImage());
                log.info("Đã upload ảnh thành công: {}", imageUrl);
            }

            // Tạo entity Dish từ DTO
            Dish dish =Dish.builder()
                    .name(dishDTO.getName())
                    .description(dishDTO.getDescription())
                    .price(dishDTO.getPrice())
                    .status(dishDTO.getStatus() != null ? dishDTO.getStatus() : "ACTIVE")
                    .imageUrl(imageUrl)
                    .build();

            Dish savedDish = dishRepository.save(dish);
            log.info("Đã tạo món ăn mới với ID: {}", savedDish.getId());

            return savedDish;

        } catch (IOException e) {
            log.error("Lỗi khi upload ảnh: {}", e.getMessage());
            throw new BadRequestException("Lỗi khi upload ảnh: " + e.getMessage());
        }
    }

    /**
     * Cập nhật món ăn
     */
    public Dish updateDish(Long id, DishDTO dishDTO) {
        try {
            // Tìm món ăn theo ID
            Dish existingDish = dishRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Không tìm thấy món ăn với ID: " + id));

            // Lưu URL ảnh cũ để xóa sau
            String oldImageUrl = existingDish.getImageUrl();

            // Cập nhật thông tin
            existingDish.setName(dishDTO.getName());
            existingDish.setDescription(dishDTO.getDescription());
            existingDish.setPrice(dishDTO.getPrice());
            existingDish.setStatus(dishDTO.getStatus() != null ? dishDTO.getStatus() : existingDish.getStatus());

            // Xử lý ảnh mới nếu có
            if (dishDTO.getImage() != null && !dishDTO.getImage().isEmpty()) {
                // Upload ảnh mới
                String newImageUrl = cloudinaryService.uploadImage(dishDTO.getImage());
                existingDish.setImageUrl(newImageUrl);

                // Xóa ảnh cũ nếu có
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    String publicId = cloudinaryService.extractPublicId(oldImageUrl);
                    if (publicId != null) {
                        cloudinaryService.deleteImage("dishes/" + publicId);
                        log.info("Đã xóa ảnh cũ: {}", oldImageUrl);
                    }
                }

                log.info("Đã upload ảnh mới: {}", newImageUrl);
            }

            Dish updatedDish = dishRepository.save(existingDish);
            log.info("Đã cập nhật món ăn với ID: {}", updatedDish.getId());

            return updatedDish;

        } catch (IOException e) {
            log.error("Lỗi khi xử lý ảnh: {}", e.getMessage());
            throw new BadRequestException("Lỗi khi xử lý ảnh: " + e.getMessage());
        }
    }

    /**
     * Xóa món ăn
     */
    public void deleteDish(Long id) {
        try {
            Dish dish = dishRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Không tìm thấy món ăn với ID: " + id));

            // Xóa ảnh trên Cloudinary nếu có
            if (dish.getImageUrl() != null && !dish.getImageUrl().isEmpty()) {
                String publicId = cloudinaryService.extractPublicId(dish.getImageUrl());
                if (publicId != null) {
                    cloudinaryService.deleteImage("dishes/" + publicId);
                    log.info("Đã xóa ảnh: {}", dish.getImageUrl());
                }
            }

            dishRepository.deleteById(id);
            log.info("Đã xóa món ăn với ID: {}", id);

        } catch (IOException e) {
            log.error("Lỗi khi xóa ảnh: {}", e.getMessage());
            throw new BadRequestException("Lỗi khi xóa ảnh: " + e.getMessage());
        }
    }

    /**
     * Lấy tất cả món ăn
     */
    @Transactional(readOnly = true)
    public List<Dish> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        log.info("Đã lấy {} món ăn", dishes.size());
        return dishes;
    }

    /**
     * Lấy món ăn theo ID
     */
    @Transactional(readOnly = true)
    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy món ăn với ID: " + id));
    }

}