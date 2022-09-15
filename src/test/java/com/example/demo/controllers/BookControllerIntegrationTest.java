package com.example.demo.controllers;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(properties = { "spring.jpa.hibernate.ddl-auto=create" })
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private Book book;

    @BeforeEach
    public void setUp(){
        book = new Book();
        book.setTitle("The Grapes of Wrath");
        book.setGenre("Teen/Drama");
        book.setPrice(19.99);
        book.setPublishYear(1920);
        var author = new Author();
        author.setName("John Steinbeck");
        book.setAuthors(Arrays.asList(author));
    }

    @WithMockUser(username = "user")
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void shouldReturnBooks() throws Exception {
        this.mockMvc.perform(get("/api/books/author/George Orwell"))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title", is("Mockingbird")))
                .andExpect(jsonPath("$[1].title", is("1984")))
                .andExpect(jsonPath("$[2].title", is("The Great Gatsby")));
    }

    @WithMockUser(username = "user")
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void shouldReturnBook() throws Exception {
        this.mockMvc.perform(get("/api/books/title/Mockingbird"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Mockingbird")));
    }

    @WithMockUser(username = "user")
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void shouldCreateBook() throws Exception {
        this.mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title", is("The Grapes of Wrath")));
        this.mockMvc.perform(get("/api/books/author/John Steinbeck"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title", is("The Grapes of Wrath")));
    }

    @WithMockUser(username = "user")
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void shouldNotCreateBookIfExists() throws Exception {
        this.mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title", is("The Grapes of Wrath")));

        this.mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().is(400));
    }

    @WithMockUser(username = "user")
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void shouldUpdateBook() throws Exception {
        var result = this.mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isCreated()).andReturn();
        var update = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Book.class);
        update.setTitle("The Grapes of Wrath - Special Edition");
        update.setPrice(29.99);
        update.getAuthors().get(0).setName("J.R.R.Tolkien");
        this.mockMvc.perform(put("/api/books").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title", is("The Grapes of Wrath - Special Edition")))
                .andExpect(jsonPath("price", is(29.99)))
                .andExpect(jsonPath("authors[0].name", is("J.R.R.Tolkien")));
    }

    @WithMockUser(username = "user")
    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void shouldNotUpdateBookIfNotExists() throws Exception {
        book.setId(-1L);
        book.setTitle("The Grapes of Wrath - Special Edition");
        book.setPrice(29.99);
        book.getAuthors().get(0).setName("J.R.R.Tolkien");
        this.mockMvc.perform(put("/api/books").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().is(400))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", is("Book with id -1 does not exist")));
    }

    @WithMockUser(username = "user")
    @Test
    public void shouldReturn404() throws Exception {
        this.mockMvc.perform(get("/api/books/random/author/George Orwell")).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    public void shouldReturn401() throws Exception {
        this.mockMvc.perform(get("/api/books/author/George Orwell")).andExpect(MockMvcResultMatchers.status().is(401));
    }

    @WithMockUser(username = "user")
    @Test
    public void shouldReturn400() throws Exception {
        book.setTitle(null);
        this.mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().is(400))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title", is("Title cannot be null or empty")));
    }
}