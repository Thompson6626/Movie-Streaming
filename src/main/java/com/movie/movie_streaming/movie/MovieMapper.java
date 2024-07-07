package com.movie.movie_streaming.movie;

import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.person.Person;
import com.movie.movie_streaming.person.dto.PersonDisplay;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieMapper {
    public MovieResponse toResponse(Movie movie){
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .rating(movie.getRating())
                .poster(movie.getPoster())
                .commentAvgRating(movie.getAvgCommentStars())
                .duration(movie.getDuration())
                .yearReleased(movie.getYearReleased().getValue())
                .build();
    }

    public Movie toMovie(SaveMovieRequest request){
        return Movie.builder()
                .title(request.title())
                .synopsis(request.synopsis())
                .rating(request.rating())
                .duration(request.duration())
                .poster(request.poster())
                .trailerLink(request.trailerLink())
                .movieLink(request.movieLink())
                .yearReleased(request.yearReleased())
                .genres(request.genres())
                .comments(new ArrayList<>())
                .build();
    }
    public MovieDisplay toDisplay(Movie movie){
        return MovieDisplay.builder()
                .title(movie.getTitle())
                .synopsis(movie.getSynopsis())
                .yearReleased(movie.getYearReleased().getValue())
                .poster(movie.getPoster())
                .trailerLink(movie.getTrailerLink())
                .movieLink(movie.getMovieLink())
                .actors(peopleToDisplay(movie.getActors()))
                .directors(peopleToDisplay(movie.getDirectors()))
                .genres(Genre.toStringSet(movie.getGenres()))
                .duration(movie.getDuration())
                .rating(movie.getRating().name())
                .build();
    }
    public <T extends Person> Set<PersonDisplay> peopleToDisplay(Set<T> person){
        return person.stream()
                .map(p -> PersonDisplay.builder()
                        .id(p.getId())
                        .fullName(p.getFullName())
                        .build()
                )
                .collect(Collectors.toSet());
    }
}
