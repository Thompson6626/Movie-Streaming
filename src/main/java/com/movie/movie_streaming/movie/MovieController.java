package com.movie.movie_streaming.movie;



import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Tag(name = "Movie")
public class MovieController {
    private final MovieService movieService;

    @Operation(
            summary = "Home Page",
            description = "Returns the a page response of movies ordered from most recently released to the oldest.",
            responses = {
                    @ApiResponse(
                            description = "Successful",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/home")
    public ResponseEntity<PageResponse<MovieResponse>> findAllMovies(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ){
        return ResponseEntity.ok(movieService.findAllMovies(page, size));
    }

    @Operation(
            summary = "Gets the movie with the given id",
            description = "Returns a movie display with the given id",
            responses = {
                    @ApiResponse(
                            description = "Successful",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Movie with id not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MovieDisplay> findById(
            @PathVariable(name = "id") Integer id
    ){
        return ResponseEntity.ok(movieService.findMovieById(id));
    }
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDisplay> findById(
            @Valid @RequestBody PostCommentRequest request,
            @PathVariable("id") Integer movieId
    ){
        return ResponseEntity.ok(movieService.addCommentToMovie(request,movieId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<MovieResponse>> findMoviesWithSearchTerm(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "s") String searchTerm
    ){
        return ResponseEntity.ok(movieService.findAllMoviesWithSearchTerm(page, size,searchTerm));
    }

    @GetMapping("/by-genres")
    public ResponseEntity<PageResponse<MovieResponse>> findAllMoviesByGenres(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "genres") Set<Genre> genres
    ){
        return ResponseEntity.ok(movieService.findAllMoviesWithGenres(page, size,genres));
    }

    @GetMapping("/actors/{id}")
    public ResponseEntity<PageResponse<MovieResponse>> findAllMoviesWithActor(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @PathVariable("id") Integer id
    ){
        return ResponseEntity.ok(movieService.findAllMoviesWithActor(page, size,id));
    }
    @GetMapping("/directors/{id}")
    public ResponseEntity<PageResponse<MovieResponse>> findAllMoviesByDirector(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @PathVariable("id") Integer id
    ){
        return ResponseEntity.ok(movieService.findAllMoviesByDirector(page, size,id));
    }
}
