package com.example.ss8.controller;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.exception.NotFoundException;
import com.example.ss8.model.dto.request.DishDTO;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.Dish;
import com.example.ss8.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping()
    public ResponseEntity<DataResponse<Dish>> createDish(@Valid @ModelAttribute DishDTO dishDTO) throws BadRequestException {
        return new ResponseEntity<>(dishService.createDish(dishDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<DataResponse<Dish>> updateDish(@PathVariable Long id, @Valid @ModelAttribute DishDTO dishDTO) throws NotFoundException, BadRequestException{
        return new ResponseEntity<>(dishService.updateDish(id, dishDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<String>> deleteDish(@PathVariable Long id) throws NotFoundException{
        dishService.deleteDish(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<Dish>>> getAllDishes() {
        return new ResponseEntity<>(dishService.getAllDishes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(dishService.getDishById(id), HttpStatus.OK);
    }
}