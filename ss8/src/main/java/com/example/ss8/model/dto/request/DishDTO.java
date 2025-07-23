package com.example.ss8.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DishDTO {
    @NotBlank(message = "Tên món ăn không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private Double price;

    @NotBlank(message = "Trạng thái không được để trống")
    private String status;

    private MultipartFile image;
}