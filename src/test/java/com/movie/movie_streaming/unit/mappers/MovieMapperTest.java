package com.movie.movie_streaming.unit.mappers;


import com.movie.movie_streaming.movie.Genre;
import com.movie.movie_streaming.movie.Movie;
import com.movie.movie_streaming.movie.MovieMapper;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(InstancioExtension.class)
public class MovieMapperTest {

    private MovieMapper movieMapper;
    @BeforeEach
    public void init(){
        movieMapper = new MovieMapper();
    }
    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.COLLECTION_MIN_SIZE, 1)
            .set(Keys.COLLECTION_MAX_SIZE, 5)
            .set(Keys.INTEGER_MIN, 1)
            .set(Keys.INTEGER_MAX, Integer.MAX_VALUE);


    @Test
    void correctMovieRequestToMovieMapping(){
        SaveMovieRequest request = Instancio.of(SaveMovieRequest.class)
                .generate(field(SaveMovieRequest::directors), gen -> gen.collection().maxSize(0))
                .generate(field(SaveMovieRequest::actors), gen -> gen.collection().maxSize(0))
                .create();

        Movie movie = movieMapper.toMovie(request);
        assertEquals(request.title(),movie.getTitle());
        assertEquals(request.synopsis(),movie.getSynopsis());
        assertEquals(request.movieLink(),movie.getMovieLink());
        assertEquals(request.poster(),movie.getPoster());
        assertEquals(request.trailerLink(),movie.getTrailerLink());
        assertEquals(request.duration(),movie.getDuration());
        assertEquals(request.rating(),movie.getRating());
        assertEquals(request.yearReleased(),movie.getYearReleased());
        assertIterableEquals(request.genres(),movie.getGenres());
        assertTrue(movie.getComments().isEmpty());
    }

    @Test
    void correctMovieToMovieDisplay(){
        Movie movie = Instancio.of(Movie.class)
                .generate(field(Movie::getComments), gen -> gen.collection().maxSize(0))
                .generate(field(Movie::getDirectors), gen -> gen.collection().maxSize(0))
                .generate(field(Movie::getActors), gen -> gen.collection().maxSize(0))
                .create();

        MovieDisplay display = movieMapper.toDisplay(movie);

        Set<Genre> mapped = display.getGenres().stream()
                .map(Genre::fromString)
                .collect(Collectors.toSet());

        assertEquals(movie.getTitle(),display.getTitle());
        assertEquals(movie.getSynopsis(),display.getSynopsis());
        assertEquals(movie.getMovieLink(),display.getMovieLink());
        assertEquals(movie.getPoster(),display.getPoster());
        assertEquals(movie.getTrailerLink(),display.getTrailerLink());
        assertEquals(movie.getDuration(),display.getDuration());
        assertEquals(movie.getRating().name(),display.getRating());
        assertEquals(movie.getYearReleased().getValue(),display.getYearReleased());
        //assertEquals(movie.getComments().size(), display.getComments().size());
        assertEquals(movie.getActors().size(), display.getActors().size());
        assertEquals(movie.getDirectors().size(), display.getDirectors().size());
        assertThat(movie.getGenres()).containsExactlyInAnyOrderElementsOf(mapped);
    }

    @Test
    void correctMovieToMovieResponse(){
        Movie movie = Instancio.of(Movie.class)
                .generate(field(Movie::getComments), gen -> gen.collection().maxSize(0))
                .generate(field(Movie::getDirectors), gen -> gen.collection().maxSize(0))
                .generate(field(Movie::getActors), gen -> gen.collection().maxSize(0))
                .create();

        MovieResponse response = movieMapper.toResponse(movie);
        assertEquals(movie.getId(),response.getId());
        assertEquals(movie.getTitle(),response.getTitle());
        assertEquals(movie.getRating(),response.getRating());
        assertEquals(movie.getPoster(),response.getPoster());
        assertEquals(movie.getAvgCommentStars(),response.getCommentAvgRating());
        assertEquals(movie.getDuration(),response.getDuration());
        assertEquals(movie.getYearReleased().getValue(),response.getYearReleased());
    }




}
