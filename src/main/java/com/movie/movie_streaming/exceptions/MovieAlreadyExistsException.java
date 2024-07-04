package com.movie.movie_streaming.exceptions;

public class MovieAlreadyExistsException extends RuntimeException{
    public MovieAlreadyExistsException() {
        super("Movie already exists and was not saved.");
    }
}
