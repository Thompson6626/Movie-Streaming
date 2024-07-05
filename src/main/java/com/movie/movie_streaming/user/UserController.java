package com.movie.movie_streaming.user;


import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.user.dto.ChangePasswordRequest;
import com.movie.movie_streaming.user.dto.UpdateEmailRequest;
import com.movie.movie_streaming.user.dto.UpdateUsernameRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Change Password",
            description = "Allows the connected user to change their password.",
            responses = {
                    @ApiResponse(
                            description = "Password changed successfully",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Incorrect current password",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "New password does not match confirmation password",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    @Operation(
            summary = "Find Favourites",
            responses = {
                    @ApiResponse(
                            description = "Returns a page response uf the user's favourite movies",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/favourites")
    public ResponseEntity<PageResponse<MovieResponse>> findFavourites(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication authentication
    ){


        return ResponseEntity.ok(userService.findAllFavourites(page, size,authentication));
    }
    @Operation(
            summary = "Add to Favourites",
            responses = {
                    @ApiResponse(
                            description = "Movie added to favourites successfully",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Movie not found",
                            responseCode = "404",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Movie is already in favourites",
                            responseCode = "409",
                            content = @Content
                    )
            }
    )
    @PostMapping("/favourites/{movieId}")
    public ResponseEntity<?> addToFavourites(
            @PathVariable("movieId") Integer movieId,
            Authentication authentication
    ){
        userService.addToFavourites(movieId,authentication);
        return ResponseEntity.ok().build();
    }
    @Operation(
            summary = "Remove from Favourites",
            responses = {
                    @ApiResponse(
                            description = "Movie removed from favourites successfully",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Movie not found",
                            responseCode = "404",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Movie was not present in favourites",
                            responseCode = "409",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/favourites/{movieId}")
    public ResponseEntity<?> deleteFromFavourites(
            @PathVariable("movieId") Integer movieId,
            Authentication authentication
    ){
        userService.removeFromFavourites(movieId,authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Change Username",
            description = "Allows the connected user to change their username.",
            responses = {
                    @ApiResponse(
                            description = "Username changed successfully",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Username is already taken",
                            responseCode = "409",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/username")
    public ResponseEntity<?> changeUsername(
            @RequestBody UpdateUsernameRequest request,
            Authentication connectedUser
    ){
        userService.changeUsername(request,connectedUser);
        return ResponseEntity.ok().build();
    }
    @Operation(
            summary = "Change Email",
            description = "Allows the connected user to change their email.",
            responses = {
                    @ApiResponse(
                            description = "Email changed successfully",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Email is already taken",
                            responseCode = "409",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/email")
    public ResponseEntity<?> changeEmail(
            @RequestBody UpdateEmailRequest request,
            Authentication connectedUser
    ){
        userService.changeEmail(request,connectedUser);
        return ResponseEntity.ok().build();
    }

}
