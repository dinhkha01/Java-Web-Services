package com.example.ss4.service;

import com.example.ss4.entity.Book;
import com.example.ss4.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public Page<Book> getAllBooksSortById(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Page<Book> getBooksByTitle(String title, int page, int size){
        return bookRepository.findByTitleContainingIgnoreCase(title,PageRequest.of(page,size, Sort.by("id").descending()));
    }
    public Book addBook(Book book){
        return bookRepository.save(book);
    }
    public Book updateBook(Book book) {
        if (book.getId() == null || !bookRepository.existsById(book.getId())) {
            throw new IllegalArgumentException("Book with given ID does not exist");
        }
        return bookRepository.save(book);
    }
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book with given ID does not exist");
        }
        bookRepository.deleteById(id);
    }
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

}
