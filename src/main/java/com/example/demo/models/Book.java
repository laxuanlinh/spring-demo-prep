package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Title cannot be null or empty")
    @Column(name = "title")
    private String title;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @NotNull(message = "Id cannot be nul")
    @Size(min = 1, message = "Author cannot be null or empty")
    private List<Author> authors;

    @NotNull(message = "Publish year cannot be nul")
    @Column(name = "publish_year")
    private Integer publishYear;
    @NotNull(message = "Price cannot be nul")
    @Column(name = "price")
    private Double price;
    @NotBlank(message = "Genre cannot be null or empty")
    @Column(name = "genre")
    private String genre;

    public Book(String title, List<Author> authors, Integer publishYear, Double price, String genre) {
        this.title = title;
        this.authors = authors;
        this.publishYear = publishYear;
        this.price = price;
        this.genre = genre;
    }
}
