package com.movie.movie_streaming.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class ActorNotFoundException extends EntityNotFoundException {
    public ActorNotFoundException(int id) {
        super("Actor with id " + id +" was not found");
    }
}
