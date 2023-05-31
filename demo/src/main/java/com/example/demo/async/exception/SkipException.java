package com.example.demo.async.exception;

public class SkipException extends RuntimeException {
    public SkipException() {
        super();
    }

    public SkipException(String message) {

        super(message);
    }


}
