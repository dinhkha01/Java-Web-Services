package com.example.demo.controller;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.response.DataResponse;
import com.example.demo.model.entity.CreditCard;
import com.example.demo.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/creditcards")
public class CreditCardController {
    private final CreditCardService creditCardService;
    @PostMapping
    public ResponseEntity<DataResponse<CreditCard>> createCreditCard(@RequestBody CreditCard creditCard) throws NotFoundException {
        return new ResponseEntity<>(creditCardService.createCreditCard(creditCard), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<CreditCard>> updateSpendingLimit(@PathVariable Long id,@RequestBody Double spendingLimit) throws NotFoundException {
        return new ResponseEntity<>(creditCardService.updateSpendingLimit(id, spendingLimit), HttpStatus.OK);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<DataResponse<CreditCard>> updateStatus(@PathVariable Long id,@RequestBody String status) throws NotFoundException {
        return new ResponseEntity<>(creditCardService.updateStatus(id, status), HttpStatus.OK);
    }
}
