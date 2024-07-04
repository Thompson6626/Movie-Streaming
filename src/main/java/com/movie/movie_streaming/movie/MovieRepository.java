package com.movie.movie_streaming.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie,Integer> {

    @Query("""
            SELECT movie
            FROM Movie movie
            WHERE LOWER(movie.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """)
    Page<Movie> findAllMoviesByTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    @Query("""
            SELECT movie 
            FROM Movie movie 
            JOIN movie.genres g 
            WHERE g IN :genres 
            """)
    Page<Movie> findAllMoviesWithGenres(@Param("genres") Set<Genre> genres, Pageable pageable);



}
