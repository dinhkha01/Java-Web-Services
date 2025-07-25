package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.response.DataResponse;
import com.example.demo.model.entity.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService  {
    public final AccountRepository accountRepository;

    public DataResponse<List<Account>> getAllAccounts(){
        return DataResponse.<List<Account>>builder()
                .code(200)
                .key("accounts")
                .data(accountRepository.findAll())
                .build();
    }

    public DataResponse<Account> getAccountByCccd(String cccd) throws NotFoundException {
        Account account = accountRepository.findAccountByCccd(cccd);
        if(account == null) {
            throw new NotFoundException("Không tìm thấy tài khoản với CCCD: " + cccd);
        }
        return DataResponse.<Account>builder()
                .code(200)
                .key("account")
                .data(account)
                .build();
    }
    public DataResponse<Account> createAccount(Account account) {
        return DataResponse.<Account>builder()
                .code(201)
                .key("account")
                .data(accountRepository.save(account))
                .build();
    }
    public DataResponse<Account> updateAccount(UUID id, Account request) throws NotFoundException {
        Account oldAccount = accountRepository.findById(id).get();
        log.debug("Thoong tin cu" + oldAccount);
        if(!accountRepository.existsById(id)){
            throw new NotFoundException("Không tìm thấy tài khoản với ID: " + id);
        }
        log.debug("Thông tin mới: " + request);
        request.setId(id);
        return DataResponse.<Account>builder()
                .code(200)
                .key("account")
                .data(accountRepository.save(request))
                .build();
    }
    public DataResponse<Void> delete(UUID id) throws NotFoundException{
        if(!accountRepository.existsById(id)){
            throw new NotFoundException("Không tìm thấy tài khoản với ID: " + id);
        }
        accountRepository.deleteById(id);
        return DataResponse.<Void>builder()
                .code(204)
                .key("message")
                .data(null)
                .build();
    }
}
