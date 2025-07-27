package com.example.demo.repository;

import com.example.demo.model.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    boolean existsCreditCardByAccount_Id(Long accountId);
}
