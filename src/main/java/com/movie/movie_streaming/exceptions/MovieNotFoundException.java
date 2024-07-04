package com.movie.movie_streaming.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class MovieNotFoundException extends EntityNotFoundException {
    public MovieNotFoundException(int id) {
        super("Movie with id "+ id +" was not found");
    }
}
