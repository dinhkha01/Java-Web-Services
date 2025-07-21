package com.example.ss6.service;

import com.example.ss6.entity.Book;
import com.example.ss6.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService implements IBookService{
    @Autowired
    BookRepository bookRepository;
    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book updateBook(int id,Book request) {
        Book book = bookRepository.findById(id).get();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setPrice(request.getPrice());
            return bookRepository.save(book);
    }
}
