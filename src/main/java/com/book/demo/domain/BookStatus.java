package com.book.demo.domain;

public enum BookStatus {
    READ("read"),
    READING("reading"),
    TO_READ("to_read");

    private final String status;

    BookStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
