package com.example.ss14.repository;

import com.example.ss14.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IAccountRepository extends JpaRepository<User,Long> {
    @Query("select u from User u where u.username = :username or u.email = :username ")
    Optional<User> loadUserByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
