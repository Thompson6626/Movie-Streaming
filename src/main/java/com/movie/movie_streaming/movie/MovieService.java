package com.movie.movie_streaming.movie;



import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.movie.dto.UpdateMovieRequest;

import java.util.Set;

public interface MovieService {

    MovieDisplay saveMovie(SaveMovieRequest request);
    void deleteById(Integer id);
    MovieDisplay updateMovie(Integer id, UpdateMovieRequest request);
    MovieDisplay findMovieById(Integer id);
    PageResponse<MovieResponse> findAllMovies(int page, int size);
    PageResponse<MovieResponse> findAllMoviesWithSearchTerm(int page, int size, String name);
    PageResponse<MovieResponse> findAllMoviesWithGenres(int page, int size, Set<Genre> genres);
    PageResponse<MovieResponse> findAllMoviesWithActor(int page, int size, Integer id);
    PageResponse<MovieResponse> findAllMoviesByDirector(int page, int size, Integer id);
    CommentDisplay addCommentToMovie(PostCommentRequest request, Integer movieId);
}
