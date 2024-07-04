package com.movie.movie_streaming.comment.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCommentRequest(
        @NotNull(message = "Id is mandatory")
        @Positive()
        Integer id,
        @NotBlank(message = "Body cannot be empty")
        String body
){
}
