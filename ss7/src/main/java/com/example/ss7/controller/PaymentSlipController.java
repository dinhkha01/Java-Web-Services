package com.example.ss7.controller;

import com.example.ss7.entity.PaymentSlip;
import com.example.ss7.service.PayMentSlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paymentslips")
public class PaymentSlipController {
    @Autowired
    private PayMentSlipService payMentSlipService;

    @GetMapping
    public ResponseEntity<List<PaymentSlip>> getAllPaymentSlips() {
        return new ResponseEntity<>(payMentSlipService.getAllPaymentSlips(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<PaymentSlip> addPaymentSlip(@RequestBody PaymentSlip paymentSlip) {
        return new ResponseEntity<>(payMentSlipService.addPaymentSlip(paymentSlip), HttpStatus.CREATED);
    }
}
