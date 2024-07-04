package com.movie.movie_streaming.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class DirectorNotFoundException extends EntityNotFoundException {

    public DirectorNotFoundException(int id) {
        super("Director with id "+id+" was not found");
    }
}
