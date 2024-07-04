package com.movie.movie_streaming.role;


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

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserAuthorities(
            @PathVariable("userId") Integer userId,
            @Valid @RequestBody UpdateRoleRequest request
    ){
        admingService.updateUserAuthorities(userId,request);
        return ResponseEntity.ok().build();
    }

}
