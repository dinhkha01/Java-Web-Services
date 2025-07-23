package com.example.ss8.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTO {
    @NotNull(message = "ID khách hàng không được để trống")
    private Long customerId;

    @NotNull(message = "ID nhân viên không được để trống")
    private Long employeeId;

    @NotEmpty(message = "Chi tiết đơn hàng không được để trống")
    @Valid
    private List<OrderDetailDTO> orderDetails;
}