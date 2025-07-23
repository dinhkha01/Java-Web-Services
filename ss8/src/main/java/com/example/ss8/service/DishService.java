package com.example.ss8.service;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.exception.NotFoundException;
import com.example.ss8.model.dto.request.DishDTO;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.Dish;
import com.example.ss8.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public DataResponse<Dish> createDish(DishDTO dishDTO) throws BadRequestException{
        // Kiểm tra tên món ăn đã tồn tại
        if (dishRepository.existsByNameIgnoreCase(dishDTO.getName())) {
            throw new BadRequestException("Tên món ăn đã tồn tại",
                    Map.of("name", "Món ăn với tên này đã có trong hệ thống"));
        }

        // Upload ảnh lên Cloudinary
        String imageUrl = null;
        if (dishDTO.getImage() != null && !dishDTO.getImage().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(dishDTO.getImage());
        }

        // Tạo đối tượng Dish
        Dish dish = new Dish();
        dish.setName(dishDTO.getName());
        dish.setDescription(dishDTO.getDescription());
        dish.setPrice(dishDTO.getPrice());
        dish.setStatus(dishDTO.getStatus());
        dish.setImage(imageUrl);

        return DataResponse.< Dish > builder()
                .key("Dish")
                .data(dishRepository.save(dish))
                .build();
    }

    @Transactional
    public DataResponse<Dish> updateDish(Long id, DishDTO dishDTO) throws NotFoundException, BadRequestException {
        Dish existingDish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy món ăn với ID: " + id));

        // Kiểm tra tên món ăn trùng lặp (ngoại trừ món ăn hiện tại)
        if (!existingDish.getName().equalsIgnoreCase(dishDTO.getName()) &&
                dishRepository.existsByNameIgnoreCase(dishDTO.getName())) {
            throw new BadRequestException("Tên món ăn đã tồn tại",
                    Map.of("name", "Món ăn với tên này đã có trong hệ thống"));
        }

        // Xử lý ảnh nếu có ảnh mới
        String imageUrl = existingDish.getImage();
        if (dishDTO.getImage() != null && !dishDTO.getImage().isEmpty()) {
            // Xóa ảnh cũ nếu có
            if (existingDish.getImage() != null) {
                cloudinaryService.deleteImage(existingDish.getImage());
            }
            // Upload ảnh mới
            imageUrl = cloudinaryService.uploadImage(dishDTO.getImage());
        }

        // Cập nhật thông tin món ăn
        existingDish.setName(dishDTO.getName());
        existingDish.setDescription(dishDTO.getDescription());
        existingDish.setPrice(dishDTO.getPrice());
        existingDish.setStatus(dishDTO.getStatus());
        existingDish.setImage(imageUrl);
        return DataResponse.<Dish>builder()
                .key("dish")
                .data(dishRepository.save(existingDish))
                .build();
    }

    @Transactional
    public void deleteDish(Long id) throws NotFoundException{

        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy món ăn với ID: " + id));

        // Xóa ảnh từ Cloudinary nếu có
        if (dish.getImage() != null) {
            cloudinaryService.deleteImage(dish.getImage());
        }

        dishRepository.delete(dish);
    }

    public DataResponse<List<Dish>>  getAllDishes() {
        DataResponse<List<Dish>> response = DataResponse.<List<Dish>>builder()
                .key("dishs")
                .data(dishRepository.findAll())
                .build();
        return response;
    }

    public Dish getDishById(Long id) throws NotFoundException {
        return dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy món ăn với ID: " + id));
    }
}