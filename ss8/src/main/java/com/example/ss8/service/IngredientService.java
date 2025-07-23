package com.example.ss8.service;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.exception.NotFoundException;
import com.example.ss8.model.dto.request.IngredientDTO;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.Ingredient;
import com.example.ss8.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public DataResponse<Ingredient> createIngredient(IngredientDTO ingredientDTO) throws BadRequestException {
        // Kiểm tra tên nguyên liệu đã tồn tại
        if (ingredientRepository.existsByNameIgnoreCase(ingredientDTO.getName())) {
            throw new BadRequestException("Tên nguyên liệu đã tồn tại",
                    Map.of("name", "Nguyên liệu với tên này đã có trong hệ thống"));
        }

        // Upload ảnh lên Cloudinary
        String imageUrl = null;
        if (ingredientDTO.getImage() != null && !ingredientDTO.getImage().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(ingredientDTO.getImage());
        }

        // Tạo đối tượng Ingredient
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.getName());
        ingredient.setStock(ingredientDTO.getStock());
        ingredient.setExpiry(ingredientDTO.getExpiry());
        ingredient.setImage(imageUrl);

        return DataResponse.<Ingredient>builder()
                .key("ingredient")
                .data(ingredientRepository.save(ingredient))
                .build();
    }

    @Transactional
    public DataResponse<Ingredient> updateIngredient(Long id, IngredientDTO ingredientDTO)
            throws NotFoundException, BadRequestException {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nguyên liệu với ID: " + id));

        // Kiểm tra tên nguyên liệu trùng lặp (ngoại trừ nguyên liệu hiện tại)
        if (!existingIngredient.getName().equalsIgnoreCase(ingredientDTO.getName()) &&
                ingredientRepository.existsByNameIgnoreCase(ingredientDTO.getName())) {
            throw new BadRequestException("Tên nguyên liệu đã tồn tại",
                    Map.of("name", "Nguyên liệu với tên này đã có trong hệ thống"));
        }

        // Xử lý ảnh nếu có ảnh mới
        String imageUrl = existingIngredient.getImage();
        if (ingredientDTO.getImage() != null && !ingredientDTO.getImage().isEmpty()) {
            // Xóa ảnh cũ nếu có
            if (existingIngredient.getImage() != null) {
                cloudinaryService.deleteImage(existingIngredient.getImage());
            }
            // Upload ảnh mới
            imageUrl = cloudinaryService.uploadImage(ingredientDTO.getImage());
        }

        // Cập nhật thông tin nguyên liệu
        existingIngredient.setName(ingredientDTO.getName());
        existingIngredient.setStock(ingredientDTO.getStock());
        existingIngredient.setExpiry(ingredientDTO.getExpiry());
        existingIngredient.setImage(imageUrl);

        return DataResponse.<Ingredient>builder()
                .key("ingredient")
                .data(ingredientRepository.save(existingIngredient))
                .build();
    }

    @Transactional
    public void deleteIngredient(Long id) throws NotFoundException {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nguyên liệu với ID: " + id));

        // Xóa ảnh từ Cloudinary nếu có
        if (ingredient.getImage() != null) {
            cloudinaryService.deleteImage(ingredient.getImage());
        }

        ingredientRepository.delete(ingredient);
    }

    public DataResponse<List<Ingredient>> getAllIngredients() {
        return DataResponse.<List<Ingredient>>builder()
                .key("ingredients")
                .data(ingredientRepository.findAll())
                .build();
    }

    public Ingredient getIngredientById(Long id) throws NotFoundException {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nguyên liệu với ID: " + id));
    }
}