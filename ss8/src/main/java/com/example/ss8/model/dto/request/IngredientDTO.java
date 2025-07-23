package com.example.ss8.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class IngredientDTO {

    @NotBlank(message = "Tên nguyên liệu không được để trống")
    private String name;

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho phải lớn hơn hoặc bằng 0")
    private Integer stock;

    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate expiry;

    private MultipartFile image;
}