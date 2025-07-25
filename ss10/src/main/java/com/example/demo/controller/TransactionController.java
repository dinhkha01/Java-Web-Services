package com.example.demo.controller;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.response.DataResponse;
import com.example.demo.model.entity.Transaction;
import com.example.demo.model.entity.TransferRequest;
import com.example.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<DataResponse<Transaction>> transferMoney(@RequestBody TransferRequest request)
            throws NotFoundException, BadRequestException {
        DataResponse<Transaction> response = transactionService.transferMoney(
                request.getSenderId(),
                request.getReceiverId(),
                request.getAmount(),
                request.getNote()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}