package com.example.demo.controllers;

import com.example.demo.models.Book;
import com.example.demo.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/api/admin/deletebook")
    public ResponseEntity<Book> updateBook(){
        System.out.println("yay deleted");
        return new ResponseEntity<>(new Book(), HttpStatus.OK);
    }

}
