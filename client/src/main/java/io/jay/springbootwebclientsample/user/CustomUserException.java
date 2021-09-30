package io.jay.springbootwebclientsample.user;

public class CustomUserException extends RuntimeException {
    public CustomUserException(String message) {
        super(message);
    }
}
