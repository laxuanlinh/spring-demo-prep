package com.example.demo.services;

import com.example.demo.exceptions.InvalidInputException;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public Book getBookByTitle(String title){
        return bookRepository.findByTitle(title);
    }

    public List<Book> getBookByAuthor(String name){
        return bookRepository.findByAuthor(name);
    }

    public void deleteBook(Long id){
        var book = bookRepository.findById(id).orElseThrow(()->new InvalidInputException("Book with id "+id+" does not exist"));
        bookRepository.delete(book);
    }

    @Transactional
    public Book createBook(Book book) {
        var existingBook = bookRepository.findByTitle(book.getTitle());
        if (existingBook!=null){
            throw new InvalidInputException("The book "+book.getTitle()+" already exists");
        }
        for (Author author : book.getAuthors()){
            var existingAuthor = authorRepository.findByName(author.getName());
            author.setId(Objects.requireNonNullElseGet(existingAuthor, () -> authorRepository.save(author)).getId());
        }
        return bookRepository.save(book);
    }

    public Book updateBook(Book book){
        bookRepository.findById(book.getId()).orElseThrow(()->new InvalidInputException("Book with id "+book.getId()+" does not exist"));
        for (Author author : book.getAuthors()){
            var existingAuthor = authorRepository.findByName(author.getName());
            author.setId(Objects.requireNonNullElseGet(existingAuthor, () -> authorRepository.save(author)).getId());
        }
        return bookRepository.save(book);
    }
}
