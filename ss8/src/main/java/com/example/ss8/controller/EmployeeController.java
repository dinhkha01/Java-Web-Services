package com.example.ss8.controller;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.exception.NotFoundException;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.Employee;
import com.example.ss8.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<DataResponse<List<Employee>>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<DataResponse<Employee>> createEmployee(@Valid @RequestBody Employee employee)  {
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<Employee>> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) throws BadRequestException, NotFoundException {
        return new ResponseEntity<>(employeeService.updateEmployee(id, employee), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<String>> deleteEmployee(@PathVariable Long id) throws NotFoundException {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
