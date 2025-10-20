package com.openclassrooms.mddapi.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() { super(); }
    public UsernameNotFoundException(String message) {
        super(message);
    }
    public UsernameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
