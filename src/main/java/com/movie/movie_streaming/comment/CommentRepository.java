package com.movie.movie_streaming.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query("""
            SELECT c 
            FROM Comment c 
            WHERE c.createdBy = :username
            """)
    Page<Comment> findAllByCreatedBy(@Param("username") String username, Pageable pageable);

    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.movie.id = :movieId
            """)
    Page<Comment> findAllWithMovieId(@Param("movieId") Integer movieId, Pageable pageable);
}
