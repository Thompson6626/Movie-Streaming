package com.movie.movie_streaming.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(
            summary = "Registers a new user",
            responses = {
                    @ApiResponse(
                            description = "Successful registration and a validation email with the activation code has been sent to the user's email",
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
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }
    @Operation(
            summary = "Authenticates the user",
            responses = @ApiResponse(
                    description = "Successful authentication",
                    responseCode = "200"
            )
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @Operation(
            summary = "Activates the user's account",
            description = "Given the token in the url , attempts to activate the user's account",
            responses = {
                    @ApiResponse(
                            description = "Successful activation of user's account",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Invalid token",
                            responseCode = "409",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Expired token",
                            responseCode = "409",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    @GetMapping("/activate-account")
    public ResponseEntity<?> confirm(
            @RequestParam(name = "token") String token
    ) throws MessagingException {
        service.activateAccount(token);
        return ResponseEntity.ok().build();
    }

}
