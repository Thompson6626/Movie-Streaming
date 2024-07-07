package com.movie.movie_streaming.services;

import com.movie.movie_streaming.comment.Comment;
import com.movie.movie_streaming.comment.CommentMapper;
import com.movie.movie_streaming.comment.CommentRepository;
import com.movie.movie_streaming.comment.CommentService;
import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.UpdateCommentRequest;
import com.movie.movie_streaming.exceptions.CommentNotFoundException;
import com.movie.movie_streaming.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;
    private User mainUser;
    private Authentication authentication;
    @BeforeEach
    public void setup(){
        mainUser = User.builder()
                .id(1)
                .username("testUser")
                .password("encodedPassword")
                .build();

        authentication = mock(Authentication.class);
    }

    @Test
    public void updateComment_successful() {

        var commentId = 1;

        var request = new UpdateCommentRequest("Updated body");

        var comment = Comment.builder()
                .id(commentId)
                .body("Original body")
                .createdBy(mainUser.getRealUserName())
                .build();
        when(authentication.getPrincipal()).thenReturn(mainUser);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(commentMapper.toDisplay(comment)).thenReturn(
                CommentDisplay.builder()
                        .id(commentId)
                        .body("Updated body")
                        .createdBy(mainUser.getRealUserName())
                        .build()
        );

        CommentDisplay updatedComment = commentService.updateComment(request, commentId, authentication);

        assertNotNull(updatedComment);
        assertEquals("Updated body", updatedComment.getBody());
        assertEquals(mainUser.getRealUserName(), updatedComment.getCreatedBy());
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(any(Comment.class));
        verify(commentMapper).toDisplay(any(Comment.class));
    }

    @Test
    public void updateComment_commentNotFound() {
        var commentId = 1;
        var request = new UpdateCommentRequest("Updated body");

        when(commentRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> {
            commentService.updateComment(request, commentId, authentication);
        });

        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
        verify(commentMapper, never()).toDisplay(any(Comment.class));
    }

    @Test
    public void updateComment_unauthorizedUser() {
        var commentId = 1;
        var request = new UpdateCommentRequest("Updated body");

        var otherUser = User.builder()
                .id(2)
                .username("Otheruser")
                .password("password")
                .build();

        var comment = Comment.builder()
                .id(commentId)
                .body("Original body")
                .createdBy(otherUser.getRealUserName())
                .build();

        when(authentication.getPrincipal()).thenReturn(mainUser);


        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(RuntimeException.class, () -> {
            commentService.updateComment(request, commentId, authentication);
        });

        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
        verify(commentMapper, never()).toDisplay(any(Comment.class));
    }

    @Test
    public void forceDeleteComment_successful() {
        var commentId = 1;

        when(commentRepository.existsById(commentId)).thenReturn(true);

        assertDoesNotThrow(() -> {
            commentService.forceDeleteComment(commentId);
        });

        verify(commentRepository).existsById(commentId);
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    public void forceDeleteComment_commentNotFound() {
        var commentId = 1;

        when(commentRepository.existsById(commentId)).thenReturn(false);

        assertThrows(CommentNotFoundException.class, () -> {
            commentService.forceDeleteComment(commentId);
        });

        verify(commentRepository).existsById(commentId);
        verify(commentRepository, never()).deleteById(any());
    }
}

