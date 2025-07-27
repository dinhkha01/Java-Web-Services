package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.response.DataResponse;
import com.example.demo.model.entity.Account;
import com.example.demo.model.entity.CreditCard;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;


    public DataResponse<CreditCard> createCreditCard(CreditCard creditCard) throws NotFoundException {
        Account account = accountRepository.findById(creditCard.getAccount().getId()).orElseThrow(() -> new NotFoundException("Không tìm thấy tài khoản với ID: " + creditCard.getId()));
        if(creditCardRepository.existsCreditCardByAccount_Id(creditCard.getAccount().getId())){
            throw new NotFoundException("Tài khoản đã có thẻ tín dụng");
        }
        creditCard.setAccount(account);
        return DataResponse.<CreditCard>builder()
                .code(201)
                .key("creditCard")
                .data(creditCardRepository.save(creditCard))
                .build();
    }
    public DataResponse<CreditCard> updateSpendingLimit(Long id, Double spendingLimit) throws NotFoundException {
        CreditCard creditCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thẻ tín dụng với ID: " + id));

        creditCard.setSpendingLimit(spendingLimit);
        return DataResponse.<CreditCard>builder()
                .code(200)
                .key("creditCard")
                .data(creditCardRepository.save(creditCard))
                .build();
    }
    public DataResponse<CreditCard> updateStatus(Long id, String Status) throws NotFoundException {
        CreditCard creditCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thẻ tín dụng với ID: " + id));

        creditCard.setStatus(Status);
        return DataResponse.<CreditCard>builder()
                .code(200)
                .key("creditCard")
                .data(creditCardRepository.save(creditCard))
                .build();
    }

}
