package com.example.ss4.controller;

import com.example.ss4.entity.Book;
import com.example.ss4.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("")
    public String getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String title,
            Model model
    ){
        Page<Book> books;
        if (title != null && !title.isEmpty()) {
            books = bookService.getBooksByTitle(title.trim(), page, size);
            model.addAttribute("title", title);
        } else {
            books = bookService.getAllBooksSortById(page, size);
        }
        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", books.getTotalPages());
        return "books/list";
    }
    @GetMapping("/add")
    public String showFormAdd(Model model) {
        model.addAttribute("book", new Book());
        return "books/add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        bookService.addBook(book);
        redirectAttributes.addFlashAttribute("message", "đã thêm sách thành ooong!");
        return "redirect:/books";
    }
    @GetMapping("/edit/{id}")
    public String showFormEdit(@PathVariable Long id , Model model, RedirectAttributes redirectAttributes) {
            Optional<Book> book = bookService.getBookById(id);
            if (book.isPresent()) {
                model.addAttribute("book", book.get());
                return "books/edit";
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sách với ID: " + id);
                return "redirect:/books";
            }
    }
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        book.setId(id);
        try {
            bookService.updateBook(book);
            redirectAttributes.addFlashAttribute("message", "đã cập nhật sách thành công!");
            return "redirect:/books";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books/edit/" + id;
        }
    }

    @GetMapping("/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("message", "đã xóa sách thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books";
    }
}
