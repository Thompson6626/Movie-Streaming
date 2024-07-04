package com.movie.movie_streaming.comment;


import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.UpdateCommentRequest;
import com.movie.movie_streaming.exceptions.CommentNotFoundException;
import com.movie.movie_streaming.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    public CommentDisplay updateComment(UpdateCommentRequest request, Authentication authentication) {

        Comment comment = commentRepository.findById(request.id())
                .orElseThrow(() -> new CommentNotFoundException(request.id()));

        User user = (User) authentication.getPrincipal();

        if (!comment.getCreatedBy().equals(user.getRealUserName())){
            throw new RuntimeException("Cannot modify other user's comments");
        }

        comment.setBody(request.body());

        return commentMapper.toDisplay(commentRepository.save(comment));
    }
    public void forceDeleteComment(Integer id) {
        if (!commentRepository.existsById(id)){
            throw new CommentNotFoundException(id);
        }
        commentRepository.deleteById(id);
    }
}
