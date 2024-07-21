package com.movie.movie_streaming.role;


import com.movie.movie_streaming.role.dto.UpdateRoleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
@Tag(name = "Admin")
public class AdminController {

    private final AdminService admingService;

    @Operation(
            summary = "Update User Authorities",
            responses = {
                    @ApiResponse(
                            description = "Successfully updates the role of the user with the specified ID",
                            responseCode = "200",
                            content = @Content(schema = @Schema)
                    ),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema)
                    ),
                    @ApiResponse(
                            description = "User already has the role",
                            responseCode = "409",
                            content = @Content(schema = @Schema)
                    )
            }
    )
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserAuthorities(
            @PathVariable("userId") Integer userId,
            @Valid @RequestBody UpdateRoleRequest request
    ){
        admingService.updateUserAuthorities(userId,request);
        return ResponseEntity.ok().build();
    }

}
