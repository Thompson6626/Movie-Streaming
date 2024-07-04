package com.movie.movie_streaming.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUsernameRequest(
        @NotBlank(message = "New username cannot be empty")
        String newUsername
) {
}
