package com.movie.movie_streaming.director;


import com.movie.movie_streaming.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director,Integer>,JpaSpecificationExecutor<Director>{

    @Query("""
            SELECT d 
            FROM Director d 
            WHERE d.firstName = :firstName 
            AND d.lastName = :lastName 
            AND d.country = :country 
            AND d.birthDate = :birthDate
            """)
    Optional<Director> findUniqueDirector(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("country") String country, @Param("birthDate") LocalDate birthDate);

    @Query("""
            SELECT m 
            FROM Director d 
            JOIN d.moviesDirected m 
            WHERE d.id = :directorId
            ORDER BY m.yearReleased DESC
            """)
    Page<Movie> findMoviesByDirector(@Param("directorId") Integer directorId, Pageable pageable);
}
