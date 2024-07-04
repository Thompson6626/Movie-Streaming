package com.movie.movie_streaming.exceptions;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException() {
        super("The email address is already in use. Please choose a different email.");
    }
}
