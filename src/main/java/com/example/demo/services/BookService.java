package com.example.demo.services;

import com.example.demo.models.Book;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.el.PropertyNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book getBookByTitle(String title){
        throw new PropertyNotFoundException("sum ting wong");
    }

    public List<Book> getBookByAuthor(String name){
        return bookRepository.findByAuthor(name);
    }
}
