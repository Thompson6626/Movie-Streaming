package com.movie.movie_streaming.role;



import com.movie.movie_streaming.comment.CommentService;
import com.movie.movie_streaming.movie.MovieService;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.movie.dto.UpdateMovieRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
@RequestMapping("manager")
@Tag(name = "Manager")
public class ManagerController {

    private final MovieService movieService;
    private final CommentService commentService;

    @Operation(
            summary = "Save a new movie",
            description = "Saves a new movie to the database",
            responses = {
                    @ApiResponse(
                            description = "Movie saved successfully",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    @PostMapping("/movies")
    public ResponseEntity<MovieDisplay> saveMovie(
            @Valid @RequestBody SaveMovieRequest request
    ){
        return ResponseEntity.status(CREATED).body(movieService.saveMovie(request));
    }
    @Operation(
            summary = "Delete a movie by ID",
            description = "Deletes a movie from the database by its ID",
            responses = {
                    @ApiResponse(
                            description = "Movie deleted successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Movie not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") Integer id){
        movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Update a movie",
            description = "Updates an existing movie in the database",
            responses = {
                    @ApiResponse(
                            description = "Movie updated successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Movie not found",
                            responseCode = "404",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/movies/{id}")
    public ResponseEntity<MovieDisplay> updateMovie(
            @PathVariable("id") Integer id,
            @RequestBody UpdateMovieRequest request
        ){
        return ResponseEntity.ok(movieService.updateMovie(id,request));
    }
    @Operation(
            summary = "Force delete a comment by ID",
            description = "Forcefully deletes a comment by its ID",
            responses = {
                    @ApiResponse(
                            description = "Comment deleted successfully",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Comment not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> forceDeleteComment(
            @PathVariable("id") Integer id
    ){
        commentService.forceDeleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
