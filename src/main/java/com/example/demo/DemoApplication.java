package com.example.demo;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Override
	public void run(String... args) throws Exception {
		Author author1 = new Author();
		author1.setName("author1");
		Author author2 = new Author();
		author2.setName("this is author 2");
		authorRepository.save(author1);
		authorRepository.save(author2);
		Stream.of("Book 1", "Book about programming", "Novel and stuff")
				.map(n -> new Book(n, Arrays.asList(author1, author2)))
				.forEach(b->bookRepository.save(b));
	}
}
