package com.example.ss7.repository;

import com.example.ss7.entity.PaymentSlip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSlipRepository extends JpaRepository<PaymentSlip,Long> {
}
