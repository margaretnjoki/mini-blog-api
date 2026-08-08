package com.margaretnjoki.mini_blog_api.exception;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {
        super("Email already registered: " + email);
    }

}

