package com.movie.movie_streaming.unit.mappers;



import com.movie.movie_streaming.comment.Comment;
import com.movie.movie_streaming.comment.CommentMapper;
import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.user.User;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(InstancioExtension.class)
public class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    public void init(){
        commentMapper = new CommentMapper();
    }

    @Test
    void testToCommentDisplay() {
        User user = Instancio.create(User.class);

        Comment comment = Instancio.of(Comment.class)
                .set(field(Comment::getMovie),null)
                .set(field(Comment::getCreatedBy),user.getRealUserName())
                .create();


        CommentDisplay display = commentMapper.toDisplay(comment);

        assertEquals(comment.getId(), display.getId());
        assertEquals(comment.getStars(), display.getStars());
        assertEquals(comment.getBody(), display.getBody());
        assertEquals(comment.getLastModifiedDate(), display.getLastModifiedAt());
        assertEquals(comment.getCreatedDate(), display.getPostedAt());
        assertEquals(comment.getCreatedBy(), display.getCreatedBy());
    }

    @Test
    void testToComment() {
        PostCommentRequest request = Instancio.of(PostCommentRequest.class).create();

        Comment comment = commentMapper.toComment(request);

        assertEquals(request.stars(),comment.getStars());
        assertEquals(request.body(),comment.getBody());
    }



}
