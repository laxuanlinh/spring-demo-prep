package com.example.demo.services;

import com.example.demo.exceptions.InvalidInputException;
import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private final BookService bookService = new BookService();

    @BeforeEach
    public void setUp(){
        var book = new Book();
        book.setId(1L);
        book.setTitle("Mockingbird");
        book.setGenre("Novel");
        book.setPrice(19.99);
        book.setPublishYear(1920);
        var author = new Author();
        author.setId(1L);
        author.setName("Harper Lee");
        book.setAuthors(Arrays.asList(author));
        when(bookRepository.findByTitle("Mockingbird")).thenReturn(book);
        when(bookRepository.findByAuthor("Harper Lee")).thenReturn(Arrays.asList(book));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findByName("Harper Lee")).thenReturn(author);
        doNothing().when(bookRepository).delete(any(Book.class));
    }

    @Test
    public void testGetBookByTitle(){
        var book = bookService.getBookByTitle("Mockingbird");
        assertNotNull(book);
        assertEquals(1L, book.getId());
        assertEquals("Mockingbird", book.getTitle());
    }

    @Test
    public void testGetBookByAuthor(){
        var books = bookService.getBookByAuthor("Harper Lee");
        assertEquals(1, books.size());
        assertEquals(1L, books.get(0).getId());
        assertEquals("Mockingbird", books.get(0).getTitle());
    }

    @Test
    public void testDeleteBook(){
        bookService.deleteBook(1L);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        var thrown = assertThrows(InvalidInputException.class, ()->bookService.deleteBook(1L));
        assertEquals("Book with id 1 does not exist", thrown.getMessage());
    }

    @Test
    public void testCreateBook(){
        var book = new Book();
        book.setTitle("1984");
        book.setGenre("Novel");
        book.setPrice(19.99);
        book.setPublishYear(1920);
        var author = new Author();
        author.setId(1L);
        author.setName("Harper Lee");
        book.setAuthors(Arrays.asList(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        var createdBook = bookService.createBook(book);
        assertEquals("1984", createdBook.getTitle());
        assertEquals("Harper Lee", createdBook.getAuthors().get(0).getName());
    }

    @Test
    public void testCreateBookAlreadyExists(){
        var book = new Book();
        book.setTitle("Mockingbird");
        book.setGenre("Novel");
        book.setPrice(19.99);
        book.setPublishYear(1920);
        var author = new Author();
        author.setName("Harper Lee");
        book.setAuthors(Arrays.asList(author));
        var thrown = assertThrows(InvalidInputException.class, ()->bookService.createBook(book));
        assertEquals("The book Mockingbird already exists", thrown.getMessage());
    }

    @Test
    public void testUpdateBook(){
        var book = new Book();
        book.setId(1L);
        book.setTitle("Mockingbird");
        book.setGenre("Teen/Drama");
        book.setPrice(19.99);
        book.setPublishYear(1920);
        Author author = new Author();
        author.setName("Harper Lee");
        book.setAuthors(Arrays.asList(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        var updatedBook = bookService.updateBook(book);
        assertEquals("Mockingbird", updatedBook.getTitle());
        assertEquals("Teen/Drama", updatedBook.getGenre());
    }

    @Test
    public void testUpdateBookDoesNotExist(){
        var book = new Book();
        book.setId(2L);
        book.setTitle("Random Book");
        book.setGenre("Teen/Drama");
        book.setPrice(19.99);
        book.setPublishYear(1920);
        var author = new Author();
        author.setName("Harper Lee");
        book.setAuthors(Arrays.asList(author));
        var thrown = assertThrows(InvalidInputException.class, ()->bookService.updateBook(book));
        assertEquals("Book with id 2 does not exist", thrown.getMessage());
    }

}