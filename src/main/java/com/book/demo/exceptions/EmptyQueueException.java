package com.book.demo.exceptions;

public class EmptyQueueException extends Exception {
    public EmptyQueueException() {
        super("Processing queue is empty");
    }
}
