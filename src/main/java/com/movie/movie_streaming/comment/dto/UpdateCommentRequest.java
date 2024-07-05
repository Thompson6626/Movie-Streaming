package com.movie.movie_streaming.comment.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCommentRequest(
        @NotBlank(message = "Body cannot be empty")
        String body
){
}
