package com.movie.movie_streaming.actor;


import com.movie.movie_streaming.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor,Integer> , JpaSpecificationExecutor<Actor> {
    @Query("""
            SELECT a 
            FROM Actor a 
            WHERE a.firstName = :firstName 
            AND a.lastName = :lastName 
            AND a.country = :country 
            AND a.birthDate = :birthDate
            """)
    Optional<Actor> findUniqueActor(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("country") String country, @Param("birthDate") LocalDate birthDate);

    @Query("""
            SELECT m 
            FROM Actor a 
            JOIN a.moviesActed m 
            WHERE a.id = :actorId
            ORDER BY m.yearReleased DESC
            """)
    Page<Movie> findMoviesByActor(@Param("actorId") Integer actorId, Pageable pageable);

}

