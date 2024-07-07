package com.movie.movie_streaming.services;

import com.movie.movie_streaming.actor.Actor;
import com.movie.movie_streaming.actor.ActorRepository;
import com.movie.movie_streaming.comment.Comment;
import com.movie.movie_streaming.comment.CommentMapper;
import com.movie.movie_streaming.comment.CommentRepository;
import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.director.Director;
import com.movie.movie_streaming.director.DirectorRepository;
import com.movie.movie_streaming.exceptions.MovieAlreadyExistsException;
import com.movie.movie_streaming.exceptions.MovieNotFoundException;
import com.movie.movie_streaming.movie.*;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.movie.dto.UpdateMovieRequest;
import com.movie.movie_streaming.person.PersonMapper;
import com.movie.movie_streaming.person.dto.PersonRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ActorRepository actorRepository;
    @Mock
    private DirectorRepository directorRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private MovieMapper movieMapper;
    @Mock
    private PersonMapper personMapper;
    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    public void testSaveMovie() {
        var saveMovieRequest = SaveMovieRequest.builder()
                .title("Test Movie")
                .actors(new HashSet<>())
                .directors(new HashSet<>())
                .build();

        var movie = Movie.builder()
                .title("Test Movie")
                .build();

        var display = MovieDisplay.builder()
                .title("Display title")
                .build();

        when(movieMapper.toMovie(saveMovieRequest)).thenReturn(movie);
        when(movieRepository.exists(any(Example.class))).thenReturn(false);
        when(movieRepository.save(any(Movie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(movieMapper.toDisplay(any(Movie.class))).thenReturn(display);

        when(personMapper.toActorSet(anySet())).thenReturn(new HashSet<>());
        when(personMapper.toDirectorSet(anySet())).thenReturn(new HashSet<>());

        MovieDisplay savedMovieDisplay = movieService.saveMovie(saveMovieRequest);

        verify(movieRepository, times(2)).save(any(Movie.class));

        assertEquals("Display title", savedMovieDisplay.getTitle());
    }

    @Test
    public void testSaveMovieAlreadyExists() {
        var saveMovieRequest = SaveMovieRequest.builder()
                .title("TestMovie")
                .build();

        var movie = Movie.builder()
                .title("Test Movie")
                .build();

        when(movieMapper.toMovie(saveMovieRequest)).thenReturn(movie);
        when(movieRepository.exists(any(Example.class))).thenReturn(true);

        assertThrows(MovieAlreadyExistsException.class, () -> movieService.saveMovie(saveMovieRequest));

        verify(movieRepository, never()).save(any(Movie.class));
    }
    @Test
    public void testUpdateMovie() {
        var movieId = 1;
        var updateMovieRequest = UpdateMovieRequest.builder()
                .title("Updated Title")
                .build();

        var movie = Movie.builder()
                .id(movieId)
                .title("Original Title")
                .build();

        var display = MovieDisplay.builder()
                .title("Updated Title")
                .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(movieMapper.toDisplay(any(Movie.class))).thenReturn(display);

        MovieDisplay updatedMovieDisplay = movieService.updateMovie(movieId, updateMovieRequest);

        verify(movieRepository, times(1)).save(movie);

        assertEquals("Updated Title", updatedMovieDisplay.getTitle());
    }

    @Test
    public void testUpdateMovieNotFound() {
        var movieId = 1;
        var updateMovieRequest = UpdateMovieRequest.builder().build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(movieId, updateMovieRequest));

        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    public void testFindMovieById() {
        var movieId = 1;
        var movie = Movie.builder()
                .id(movieId)
                .title("Test Movie")
                .build();


        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        var display = MovieDisplay.builder()
                .title("Test Movie")
                .build();

        Page<Comment> commentsPage = new PageImpl<>(new ArrayList<>());
        when(commentRepository.findAllWithMovieId(movieId, PageRequest.of(0, 5, Sort.by("createdDate").descending())))
                .thenReturn(commentsPage);
        when(movieMapper.toDisplay(any(Movie.class))).thenReturn(display);

        MovieDisplay movieDisplay = movieService.findMovieById(movieId);

        assertEquals("Test Movie", movieDisplay.getTitle());
    }

    @Test
    public void testFindAllMovies() {
        Page<Movie> moviesPage = new PageImpl<>(Collections.singletonList(new Movie()));

        when(movieRepository.findAll(any(Pageable.class))).thenReturn(moviesPage);
        when(movieMapper.toResponse(any(Movie.class))).thenReturn(new MovieResponse());

        PageResponse<MovieResponse> pageResponse = movieService.findAllMovies(0, 10);

        assertNotNull(pageResponse);
        assertEquals(1, pageResponse.getContent().size());
    }


    @Test
    public void testAddComment_succesful(){
            var movieId = 1;
            var movie = Movie.builder()
                    .id(movieId)
                    .title("Test Movie")
                    .build();

            var postCommentRequest = new PostCommentRequest(4.4,"Very good movie");

            var comment = Comment.builder()
                    .id(1)
                    .body("Test comment body")
                    .movie(movie)
                    .build();

            var display = CommentDisplay.builder()
                    .body("Test comment body")
                    .build();

            when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
            when(commentMapper.toComment(postCommentRequest)).thenReturn(comment);
            when(commentRepository.save(any(Comment.class))).thenReturn(comment);
            when(commentMapper.toDisplay(comment)).thenReturn(display);

            CommentDisplay commentDisplay = movieService.addCommentToMovie(postCommentRequest, movieId);

            assertNotNull(commentDisplay);
            assertEquals("Test comment body", commentDisplay.getBody());
    }
    @Test
    public void testAddComment_movieNotFound(){
        var movieId = 1;
        var request = new PostCommentRequest(4.4,"Bad");
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () ->{
            movieService.addCommentToMovie(request,1);
        });
        verify(commentMapper,never()).toComment(any(PostCommentRequest.class));
        verify(commentRepository,never()).save(any(Comment.class));
        verify(commentMapper,never()).toDisplay(any(Comment.class));
    }

    @Test
    public void testDeleteById_successful() {
        var movieId = 1;

        when(movieRepository.existsById(movieId)).thenReturn(true);

        assertDoesNotThrow(() -> movieService.deleteById(movieId));

        verify(movieRepository, times(1)).deleteById(movieId);
    }
    @Test
    public void testDeleteById_movieNotFound() {
        var movieId = 1;

        when(movieRepository.existsById(movieId)).thenReturn(false);

        assertThrows(MovieNotFoundException.class,() ->{
            movieService.deleteById(movieId);
        });

        verify(movieRepository,never()).deleteById(movieId);
    }
}
