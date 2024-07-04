package com.movie.movie_streaming.role;



import com.movie.movie_streaming.comment.CommentService;
import com.movie.movie_streaming.movie.MovieService;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.movie.dto.UpdateMovieRequest;
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

    @PostMapping("/movies")
    public ResponseEntity<MovieDisplay> saveMovie(
            @Valid @RequestBody SaveMovieRequest request
    ){
        return ResponseEntity.status(CREATED).body(movieService.saveMovie(request));
    }
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") Integer id){
        return ResponseEntity.ok(movieService.deleteById(id));
    }
    @PatchMapping("/movies/{id}")
    public ResponseEntity<MovieDisplay> updateMovie(
            @PathVariable("id") Integer id,
            @RequestBody UpdateMovieRequest request
        ){
        return ResponseEntity.ok(movieService.updateMovie(id,request));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> forceDeleteComment(
            @PathVariable("id") Integer id
    ){
        commentService.forceDeleteComment(id);
        return ResponseEntity.ok().build();
    }
}
