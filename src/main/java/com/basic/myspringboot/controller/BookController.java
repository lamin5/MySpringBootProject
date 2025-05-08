package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        URI location = URI.create("/api/books/" + savedBook.getId());
        return ResponseEntity.created(location).body(savedBook);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBooksById(@PathVariable Long id){
        Optional<Book> optionalBook = bookRepository.findById(id);
        ResponseEntity<Book> responseEntity = optionalBook
                .map(book -> ResponseEntity.ok(book))
                .orElse(new ResponseEntity("Books Not Found", HttpStatus.NOT_FOUND));
        return responseEntity;
    }

    @GetMapping("/isbn/{isbn}")  //http://localhost:8080/api/users/id/100
    public Book getBooksByIsbn(@PathVariable String isbn){
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        Book existBook = getExistBook(optionalBook);
        return existBook;
    }

    @GetMapping("/author/{author}")  //http://localhost:8080/api/users/id/100
    public Book getBooksByAuthor(@PathVariable String author){
        Optional<Book> optionalBook = bookRepository.findByIsbn(author);
        Book existBook = getExistBook(optionalBook);
        return existBook;
    }

    private Book getExistBook(Optional<Book> optionalBook) {
        Book existBook = optionalBook
                .orElseThrow(() -> new BusinessException("Books Not Found", HttpStatus.NOT_FOUND));
        return existBook;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book existBook = getExistBook(bookRepository.findById(id));
        existBook.setTitle(updatedBook.getTitle());
        existBook.setAuthor(updatedBook.getAuthor());
        existBook.setIsbn(updatedBook.getIsbn());
        existBook.setPrice(updatedBook.getPrice());
        existBook.setPublishDate(updatedBook.getPublishDate());
        Book savedBook = bookRepository.save(existBook);
        return ResponseEntity.ok(savedBook);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book book = getExistBook(bookRepository.findById(id));
        bookRepository.delete(book);
        return ResponseEntity.noContent().build(); //status code 200
        //return ResponseEntity.noContent().build();  //status code 204
    }
}
