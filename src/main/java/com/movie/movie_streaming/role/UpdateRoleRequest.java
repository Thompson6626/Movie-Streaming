package com.movie.movie_streaming.role;


import jakarta.validation.constraints.NotBlank;

public record UpdateRoleRequest(
        @NotBlank(message = "Role cannot be empty")
        String role
) {
}
