package com.movie.movie_streaming.repositories;


import com.movie.movie_streaming.movie.Genre;
import com.movie.movie_streaming.movie.Movie;
import com.movie.movie_streaming.movie.MovieRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static com.movie.movie_streaming.movie.Genre.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@DataJpaTest
@ExtendWith(InstancioExtension.class)
@TestInstance(PER_CLASS)
public class MovieRepositoryTest {
    @MockBean
    private AuditingHandler auditingHandler;
    @Autowired
    private MovieRepository movieRepository;
    private Pageable pageable;
    @BeforeEach
    void generatePage(){
        pageable = PageRequest.of(0,10);
    }
    @BeforeAll
    void setup(){
        List<Movie> movies = List.of(
                createMovieWithTitleAndGenres("Action movie",Set.of(HORROR,ADVENTURE),1),
                createMovieWithTitleAndGenres("In action",Set.of(ACTION),2),
                createMovieWithTitleAndGenres("Another movie",Set.of(ACTION),3),
                createMovieWithTitleAndGenres("Documentary movie",Set.of(DOCUMENTARY),4),
                createMovieWithTitleAndGenres("Yet another movie!",Set.of(FOREIGN,FANTASY),5),
                createMovieWithTitleAndGenres("From space",Set.of(SCI_FI,ACTION,HORROR),6)
        );

        movieRepository.saveAll(movies);
    }

    @Test
    void correctSearchWithTerm(){
        final String searchTerm = "action";

        Page<Movie> pages = movieRepository.findAllMoviesByTerm(searchTerm,pageable);

        assertThat(pages)
                .hasSize(2)
                .allMatch(e -> e.getTitle().toLowerCase().contains(searchTerm));
    }
    @Test
    void correctSearchWithGenres(){
        final Set<Genre> genres = Set.of(ACTION, HORROR);

        Page<Movie> page = movieRepository.findAllMoviesWithGenres(genres,pageable);

        assertThat(page)
                .hasSize(4)
                .allMatch(
                        movie -> movie.getGenres().stream()
                                .anyMatch(genres::contains)
                );
    }
    private Movie createMovieWithTitleAndGenres(String title, Set<Genre> genres, int id) {
        return Instancio.of(Movie.class)
                .set(field(Movie::getId),id)
                .set(field(Movie::getTitle),title)
                .set(field(Movie::getGenres), genres)
                .generate(field(Movie::getActors), gen -> gen.collection().maxSize(0))
                .generate(field(Movie::getDirectors), gen -> gen.collection().maxSize(0))
                .generate(field(Movie::getComments), gen -> gen.collection().maxSize(0))
                .create();
    }

}
