package com.movie.movie_streaming.comment;


import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {

    public CommentDisplay toDisplay(Comment comment){
        return CommentDisplay.builder()
                .id(comment.getId())
                .stars(comment.getStars())
                .body(comment.getBody())
                .createdBy(comment.getCreatedBy())
                .postedAt(comment.getCreatedDate())
                .lastModifiedAt(comment.getLastModifiedDate())
                .build();
    }
    public Comment toComment(PostCommentRequest request){
        return Comment.builder()
                .body(request.body())
                .stars(request.stars())
                .build();
    }


}
