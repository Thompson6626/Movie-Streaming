package com.movie.movie_streaming.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException{
    public UsernameAlreadyTakenException() {
        super("The username is already taken. Please choose a different username.");
    }
}
