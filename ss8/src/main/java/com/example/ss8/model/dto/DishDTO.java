package com.example.ss8.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishDTO {

    @NotBlank(message = "Tên món ăn không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Giá món ăn không được để trống")
    @Positive(message = "Giá món ăn phải lớn hơn 0")
    private Double price;

    private String status;

    private MultipartFile image;
}