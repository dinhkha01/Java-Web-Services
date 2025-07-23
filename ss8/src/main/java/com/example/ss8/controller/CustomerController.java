package com.example.ss8.controller;

import com.example.ss8.exception.BadRequestException;
import com.example.ss8.model.dto.request.CustomerDTO;
import com.example.ss8.model.dto.response.DataResponse;
import com.example.ss8.model.entity.Customer;
import com.example.ss8.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping
    public ResponseEntity<DataResponse<List<Customer>>> getCustomer() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK) ;
    }
    @PostMapping
    public ResponseEntity<DataResponse<Customer>> createCustomer(@RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.createCustomer(customerDTO), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<Customer>> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) throws BadRequestException {
        return new ResponseEntity<>(customerService.updateCustomer(id, customerDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<String>> deleteCustomer(@PathVariable Long id) throws BadRequestException {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
