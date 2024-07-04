package com.movie.movie_streaming.movie.dto;


import com.movie.movie_streaming.Utilities.NullOrNotBlank.NullOrNotBlank;
import com.movie.movie_streaming.movie.Genre;
import com.movie.movie_streaming.movie.Rating;
import com.movie.movie_streaming.person.dto.PersonRequest;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Year;
import java.util.Set;

public record UpdateMovieRequest(
        @NullOrNotBlank(message = "Title cannot be blank")
        String title,
        @NullOrNotBlank(message = "Sinopsis cannot be blank")
        String synopsis,
        @NullOrNotBlank(message = "Rating cannot be blank")
        Rating rating,
        @NullOrNotBlank(message = "Trailer link cannot be blank")
        String trailerLink,
        @NullOrNotBlank(message = "Movie link cannot be blank")
        String movieLink,
        @NullOrNotBlank(message = "Poster link cannot be blank")
        String poster,
        @PastOrPresent(message = "Not a valid year")
        Year yearReleased,
        @Positive(message = "Not a valid duration")
        Integer duration,
        @Size(min = 1,message = "At least 1 genre is required")
        Set<Genre> genres,
        @Size(min = 1,message = "At least 1 director is required")
        Set<PersonRequest> directors,
        @Size(min = 1,message = "At least 1 actor is required")
        Set<PersonRequest> actors
){
}
