package com.videoannotator.exception;

public class SegmentOverlapException extends RuntimeException{
    public SegmentOverlapException() {
    }

    public SegmentOverlapException(String message) {
        super(message);
    }
}
