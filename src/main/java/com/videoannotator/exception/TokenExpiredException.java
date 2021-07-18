package com.videoannotator.exception;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException() {
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
