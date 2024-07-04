package com.movie.movie_streaming.movie.dto;


import com.movie.movie_streaming.movie.Genre;
import com.movie.movie_streaming.movie.Rating;
import com.movie.movie_streaming.person.dto.PersonRequest;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.Year;
import java.util.Set;

@Builder
public record SaveMovieRequest(
        @NotBlank(message = "Title is mandatory")
        String title,
        @NotBlank(message = "Synopsis is mandatory")
        String synopsis,
        @NotNull(message = "Rating is mandatory")
        Rating rating,
        String trailerLink,
        @NotBlank(message = "Movie link is mandatory")
        String movieLink,
        String poster,
        @NotNull(message = "Year released is mandatory")
        @PastOrPresent(message = "Not a valid year")
        Year yearReleased,
        @Positive(message = "Not a valid duration")
        Integer duration,
        @NotNull(message = "Genres are mandatory")
        @Size(min = 1, message = "At least one genre is required")
        Set<Genre> genres,
        @Size(min = 1, message = "At least one director is required")
        Set<PersonRequest> directors,
        @Size(min = 1, message = "At least one actor is required")
        Set<PersonRequest> actors
) {
}
