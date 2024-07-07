package com.movie.movie_streaming.movie;


import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

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
        var responses = movieService.findAllMovies(page, size);
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Gets the movie with the given id",
            responses = {
                    @ApiResponse(
                            description = "Returns a movie display with the given id",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Movie with id not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<MovieDisplay> findById(
            @PathVariable(name = "id") Integer id
    ){
        var display = movieService.findMovieById(id);
        return ResponseEntity.ok(display);
    }
    @Operation(
            summary = "Add a comment to the movie with the given id",
            responses = {
                    @ApiResponse(
                            description = "Returns the added comment display",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Movie with id not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDisplay> postComment(
            @Valid @RequestBody PostCommentRequest request,
            @PathVariable("id") Integer movieId
    ){
        var display = movieService.addCommentToMovie(request,movieId);
        return ResponseEntity.status(CREATED).body(display);
    }
    @Operation(
            summary = "Search for movies",
            description = "Returns a page response of movies matching the search term.",
            responses = {
                    @ApiResponse(
                            description = "Successful",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<PageResponse<MovieResponse>> findMoviesWithSearchTerm(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "s") @NotBlank String searchTerm
    ){
        var responses = movieService.findAllMoviesWithSearchTerm(page, size,searchTerm);
        return ResponseEntity.ok(responses);
    }
    @Operation(
            summary = "Get movies by genres",
            description = "Returns a page response of movies filtered by the given genres.",
            responses = {
                    @ApiResponse(
                            description = "Successful",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/by-genres")
    public ResponseEntity<PageResponse<MovieResponse>> findAllMoviesByGenres(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "genres") Set<Genre> genres
    ){
        var responses = movieService.findAllMoviesWithGenres(page, size,genres);
        return ResponseEntity.ok(responses);
    }
    @Operation(
            summary = "Get movies by actor",
            responses = {
                    @ApiResponse(
                            description = "Returns a page response of movies in which the specified actor has acted",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Actor with id not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    @GetMapping("/actors/{id}")
    public ResponseEntity<PageResponse<MovieResponse>> findAllMoviesWithActor(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @PathVariable("id") Integer id
    ){
        var responses = movieService.findAllMoviesWithActor(page, size,id);
        return ResponseEntity.ok(responses);
    }
    @Operation(
            summary = "Get movies by director",
            responses = {
                    @ApiResponse(
                            description = "Returns a page response of movies directed by the specified director.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Director with id not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    @GetMapping("/directors/{id}")
    public ResponseEntity<PageResponse<MovieResponse>> findAllMoviesByDirector(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @PathVariable("id") Integer id
    ){
        var responses = movieService.findAllMoviesByDirector(page, size,id) ;
        return ResponseEntity.ok(responses);
    }
}
