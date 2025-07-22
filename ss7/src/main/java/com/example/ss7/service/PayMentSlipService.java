package com.example.ss7.service;

import com.example.ss7.entity.PaymentSlip;
import com.example.ss7.repository.PaymentSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PayMentSlipService {
    @Autowired
    PaymentSlipRepository paymentSlipRepository;

    public List<PaymentSlip> getAllPaymentSlips() {
        return paymentSlipRepository.findAll();
    }
    public PaymentSlip addPaymentSlip(PaymentSlip paymentSlip) {
        paymentSlip.setCreatedAt(LocalDateTime.now());
        return paymentSlipRepository.save(paymentSlip);
    }

}
