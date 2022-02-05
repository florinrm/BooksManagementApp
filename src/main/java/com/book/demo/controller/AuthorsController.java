package com.book.demo.controller;

import com.book.demo.domain.Author;
import com.book.demo.domain.Book;
import com.book.demo.persistance.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorsController {
    @Autowired
    private BooksRepository booksRepository;

    @GetMapping
    public Set<Author> getAuthors() {
        var fetchedBooks = booksRepository.findAll();
        var booksList = new ArrayList<Book>();
        fetchedBooks.forEach(booksList::add);
        Set<Author> authors = new TreeSet<>(Comparator.comparing(Author::getName));
        booksList.forEach(book -> authors.add(
                new Author(
                        book.getAuthor(),
                        booksList.stream()
                                .filter(b -> b.getAuthor().equals(book.getAuthor()))
                                .map(Book::getTitle)
                                .collect(Collectors.toList()))));
        return authors;
    }
}
