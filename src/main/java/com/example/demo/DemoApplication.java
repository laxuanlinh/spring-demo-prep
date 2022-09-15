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
		var author1 = new Author();
		author1.setName("Harper Lee");
		var author2 = new Author();
		author2.setName("George Orwell");
		authorRepository.save(author1);
		authorRepository.save(author2);
		Stream.of("Mockingbird", "1984", "The Great Gatsby")
				.map(n -> new Book(n, Arrays.asList(author1, author2), 1920, 19.99, "Novel"))
				.forEach(b->bookRepository.save(b));
		var user = new Account("user", bCryptPasswordEncoder.encode("password"), "USER");
		var admin = new Account("admin", bCryptPasswordEncoder.encode("password"), "ADMIN");
		accountRepository.save(user);
		accountRepository.save(admin);
	}
}
