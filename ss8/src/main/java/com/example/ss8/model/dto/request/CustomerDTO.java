package com.example.ss8.model.dto.request;

import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    @NotBlank(message = "Tên khách hàng không được để trống")
    private String name;
    @Size(min = 10, max = 11, message = "Số điện thoại phải từ 10 đến 11 ký tự")
    private String phone;
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotBlank(message = "tin nhan khong dc de trong")
    private int numberOfPayments;



}
