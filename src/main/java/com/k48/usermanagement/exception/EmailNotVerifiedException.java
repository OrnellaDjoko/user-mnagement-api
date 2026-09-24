package com.k48.usermanagement.exception;

public class EmailNotVerifiedException extends  RuntimeException {

    public  EmailNotVerifiedException(String message) {
        super(message);
    }
}
