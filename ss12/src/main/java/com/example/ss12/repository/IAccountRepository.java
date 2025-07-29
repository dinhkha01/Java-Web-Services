package com.example.ss12.repository;

import com.example.ss12.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
   @Query("select a from Account a where a.username = :username or a.email = :username or a.phone = :username")
    Optional<Account> loadUserByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
}
