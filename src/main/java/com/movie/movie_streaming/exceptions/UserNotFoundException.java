package com.movie.movie_streaming.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(int id) {
        super("User with id "+ id +" was not found");
    }
}
