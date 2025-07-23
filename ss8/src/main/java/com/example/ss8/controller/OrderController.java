package com.example.ss8.controller;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.exception.NotFoundException;
import com.example.ss8.model.dto.request.OrderDTO;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.Order;
import com.example.ss8.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<DataResponse<Order>> createOrder(@Valid @RequestBody OrderDTO orderDTO)
            throws NotFoundException, BadRequestException {
        DataResponse<Order> response = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<Order>>> getAllOrders() {
        DataResponse<List<Order>> response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Order>> getOrderById(@PathVariable Long id) throws NotFoundException {
        DataResponse<Order> response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }
}