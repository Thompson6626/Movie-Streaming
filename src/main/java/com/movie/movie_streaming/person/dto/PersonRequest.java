package com.movie.movie_streaming.person.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PersonRequest(
        @NotBlank(message = "Firstname cannot be blank")
        String firstName,
        @NotBlank(message = "Lastname cannot be blank")
        String lastName,
        @NotBlank(message = "Country is mandatory")
        String country,
        @NotBlank(message = "Birthdate is mandatory")
        @PastOrPresent(message = "Insert a valid birthdate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate,
        String photoLink
) {
}
