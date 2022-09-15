package com.example.demo.controllers;

import com.example.demo.models.Book;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import javax.validation.Valid;
import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/api/books/author/{name}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String name){
        return new ResponseEntity<>(bookService.getBookByAuthor(name), HttpStatus.OK);
    }

    @GetMapping(value = "/api/books/title/{title}")
    public ResponseEntity<Book> getBookByTitle(@PathVariable String title){
        return new ResponseEntity<>(bookService.getBookByTitle(title), HttpStatus.OK);
    }

    @PostMapping(value = "/api/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book){
        return new ResponseEntity<>(bookService.createBook(book), HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/books")
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book){
        return new ResponseEntity<>(bookService.updateBook(book), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/deletebook/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
