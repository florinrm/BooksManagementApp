package com.book.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books/content")
public class BooksContentController {
    public List<String> getBooksByWords() {
        return new ArrayList<>();
    }
}
