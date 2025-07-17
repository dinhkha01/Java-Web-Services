package com.example.ss4.repository;

import com.example.ss4.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book,Long> {
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
