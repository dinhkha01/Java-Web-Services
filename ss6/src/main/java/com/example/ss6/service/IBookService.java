package com.example.ss6.service;

import com.example.ss6.entity.Book;

import java.util.List;
import java.util.Optional;

public interface IBookService {
    List<Book> findAllBooks();
    Book saveBook(Book book);
    void deleteBook(int id);
    Book updateBook(int id,Book book);
}
