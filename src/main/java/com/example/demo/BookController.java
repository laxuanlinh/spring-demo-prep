package com.example.demo;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping(value = "/api/books")
    public ResponseEntity<List<Book>> getBooks(@RequestParam(name="size")int size, @RequestParam(name="page") int page){
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<List<Book>>(bookRepository.findAll(pageable).stream().toList(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/authors")
    public ResponseEntity<List<Author>> getAuthors(@RequestParam(name="size")int size, @RequestParam(name="page") int page){
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<List<Author>>(authorRepository.findAll(pageable).stream().toList(), HttpStatus.OK);
    }

}
