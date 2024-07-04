package com.movie.movie_streaming.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PostCommentRequest (
        @Min(value = 0)
        @Max(value = 5)
        Double stars,
        @NotBlank(message = "Body cannot be blank")
        String body
){
}
