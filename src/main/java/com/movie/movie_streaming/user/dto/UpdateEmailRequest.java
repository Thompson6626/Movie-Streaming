package com.movie.movie_streaming.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UpdateEmailRequest(
        @Email(message = "Invalid email")
        @NotBlank(message = "New email cannot be blank")
        String newEmail
) {
}
