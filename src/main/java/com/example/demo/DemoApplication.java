package com.example.demo;

import com.example.demo.models.*;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

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
		Account user = new Account("user", bCryptPasswordEncoder.encode("password"), "USER");
		Account admin = new Account("admin", bCryptPasswordEncoder.encode("password"), "ADMIN");
		accountRepository.save(user);
		accountRepository.save(admin);
	}
}
