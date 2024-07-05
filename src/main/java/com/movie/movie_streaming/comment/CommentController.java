package com.movie.movie_streaming.comment;



import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.UpdateCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
@RequiredArgsConstructor
@Tag(name = "Comment")
public class CommentController {

    private final CommentService commentService;
    @Operation(
            summary = "Update a comment",
            description = "Updates an existing comment by its ID",
            responses = {
                    @ApiResponse(
                            description = "Comment updated successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Comment not found",
                            responseCode = "404",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "User not authorized to modify another user's comment",
                            responseCode = "409",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDisplay> updateComment(
            @RequestBody UpdateCommentRequest request,
            @PathVariable("commentId") Integer commentId,
            Authentication authentication
            ){
        return ResponseEntity.ok(commentService.updateComment(request,commentId,authentication));
    }

}
