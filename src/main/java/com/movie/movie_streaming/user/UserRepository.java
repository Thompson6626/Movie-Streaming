package com.movie.movie_streaming.user;


import com.movie.movie_streaming.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    @Query("""
        SELECT m
        FROM User u
        JOIN u.favourites m
        WHERE u.id = :userId
        ORDER BY m.yearReleased DESC
        """)
    Page<Movie> findFavouritesByUser(@Param("userId") Integer userId, Pageable pageable);

    @Query("""
            SELECT u 
            FROM User u 
            LEFT JOIN FETCH u.favourites
            WHERE u.id = :userId
            """)
    Optional<User> findByIdWithFavourites(@Param("userId") Integer userId);


}
