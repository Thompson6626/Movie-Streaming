package com.movie.movie_streaming.user;


import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.user.dto.ChangePasswordRequest;
import com.movie.movie_streaming.user.dto.UpdateEmailRequest;
import com.movie.movie_streaming.user.dto.UpdateUsernameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favourites")
    public ResponseEntity<PageResponse<MovieResponse>> findFavourites(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication authentication
    ){
        return ResponseEntity.ok(userService.findAllFavourites(page, size,authentication));
    }
    @PostMapping("/favourites/{movieId}")
    public ResponseEntity<?> addToFavourites(
            @PathVariable("movieId") Integer movieId,
            Authentication authentication
    ){
        userService.addToFavourites(movieId,authentication);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/favourites/{movieId}")
    public ResponseEntity<?> deleteFromFavourites(
            @PathVariable("movieId") Integer movieId,
            Authentication authentication
    ){
        userService.removeFromFavourites(movieId,authentication);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/username")
    public ResponseEntity<?> changeUsername(
            @RequestBody UpdateUsernameRequest request,
            Authentication connectedUser
    ){
        userService.changeUsername(request,connectedUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/email")
    public ResponseEntity<?> changeEmail(
            @RequestBody UpdateEmailRequest request,
            Authentication connectedUser
    ){
        userService.changeEmail(request,connectedUser);
        return ResponseEntity.ok().build();
    }

}
