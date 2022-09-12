package com.example.demo.repositories;

import com.example.demo.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByTitle(String title);

    @Query(value = "SELECT * FROM books b JOIN books_authors ba ON b.id = ba.book_id JOIN authors a ON ba.author_id = a.id WHERE a.name = ?1", nativeQuery = true)
    List<Book> findByAuthor(String name);
}
