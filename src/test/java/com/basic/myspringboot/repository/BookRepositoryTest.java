package com.basic.myspringboot.repository;

import com.basic.myspringboot.entity.Book;
import com.basic.myspringboot.entity.Customer;
import com.basic.myspringboot.exception.BusinessException;
import org.h2.api.ErrorCode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    @Rollback(value = false)
    @Disabled
    void testCreateBook(){
        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("9788956746425");
        book.setPrice(30000);
        Book addBook = bookRepository.save(book);
        book.setPublishDate(LocalDate.of(2025, 5, 7));
        assertThat(addBook).isNotNull();
        assertThat(addBook.getTitle()).isEqualTo("스프링 부트 입문");
    }
    @Test
    @Rollback(value = false)
    void testFindByIsbn(){
        Optional<Book> found = bookRepository.findByIsbn("9788956746425");
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("스프링 부트 입문");
        Book notFound = bookRepository.findByIsbn("0000000000000")
                .orElseGet(Book::new);
        assertThat(notFound.getTitle()).isNull();
    }
    @Test
    @Rollback(value = false)
    void testFindByAuthor(){
        List<Book> books = bookRepository.findByAuthor("홍길동");
        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getTitle()).isEqualTo("스프링 부트 입문");
    }
    @Test
    @Rollback(value = false)
    void testUpdateBook(){
        Book book = bookRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        book.setTitle("스프링 부트 입문");
        assertThat(book.getTitle()).isEqualTo("스프링 부트 입문");

    }

    @Test
    @Rollback(value = false)
    void testDeleteBook(){
        Book book = bookRepository.findById(10L)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        bookRepository.delete(book);
    }

}
