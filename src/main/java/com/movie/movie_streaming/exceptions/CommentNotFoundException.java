package com.movie.movie_streaming.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    public CommentNotFoundException(int id) {
        super("Comment with id "+ id +" was not found");
    }
}
