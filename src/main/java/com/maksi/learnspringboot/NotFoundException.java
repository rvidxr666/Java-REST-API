package com.maksi.learnspringboot;

public class NotFoundException extends Exception {
    public NotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
