package com.movie.movie_streaming.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.TestUtils;
import com.movie.movie_streaming.comment.Comment;
import com.movie.movie_streaming.comment.CommentRepository;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.exceptions.MovieNotFoundException;
import com.movie.movie_streaming.movie.Genre;
import com.movie.movie_streaming.movie.Movie;
import com.movie.movie_streaming.movie.MovieRepository;
import com.movie.movie_streaming.movie.MovieServiceImpl;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.movie.dto.UpdateMovieRequest;
import com.movie.movie_streaming.person.dto.PersonRequest;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.WithSettings;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.instancio.Select.field;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(roles = "MANAGER")
public class ManagerControllerIntegrationTests {
    @MockBean
    private AuditingHandler auditingHandler;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MovieServiceImpl movieService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;
    @Autowired
    public ManagerControllerIntegrationTests(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            MovieServiceImpl movieService,
            UserRepository userRepository,
            CommentRepository commentRepository,
            MovieRepository movieRepository
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.movieService = movieService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.movieRepository = movieRepository;
    }
    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.COLLECTION_MAX_SIZE, 1)
            .set(Keys.COLLECTION_MAX_SIZE, 5)
            .set(Keys.BEAN_VALIDATION_ENABLED,true)
            .set(Keys.STRING_MAX_LENGTH,20);

    @Test
    public void movieShouldBeCreated() throws Exception {

        PersonRequest actor = Instancio.create(PersonRequest.class);
        PersonRequest director = Instancio.create(PersonRequest.class);

        SaveMovieRequest request = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::actors), Set.of(actor))
                .set(field(SaveMovieRequest::directors), Set.of(director))
                .create();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/manager/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(request.title()))
                .andExpect(jsonPath("$.synopsis").value(request.synopsis()))
                .andExpect(jsonPath("$.rating").value(request.rating().name()))
                .andExpect(jsonPath("$.movieLink").value(request.movieLink()))
                .andExpect(jsonPath("$.trailerLink").value(request.trailerLink()))
                .andExpect(jsonPath("$.poster").value(request.poster()))
                .andExpect(jsonPath("$.genres").isArray())
                .andExpect(jsonPath("$.genres", containsInAnyOrder(Genre.toStringSet(request.genres()).toArray())))
                .andExpect(jsonPath("$.yearReleased").value(request.yearReleased().getValue()))
                .andExpect(jsonPath("$.comments").isEmpty())
                .andExpect(jsonPath("$.directors[0].fullName").value(director.firstName()+ " " + director.lastName()))
                .andExpect(jsonPath("$.actors[0].fullName").value(actor.firstName()+ " " + actor.lastName()));
    }

    @Test
    public void deleteMovieShouldWork() throws Exception {
        SaveMovieRequest request = Instancio.create(SaveMovieRequest.class);
        movieService.saveMovie(request);

        mockMvc.perform(delete("/manager/movies/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMovieShouldWork() throws Exception {
        SaveMovieRequest request = Instancio.create(SaveMovieRequest.class);
        movieService.saveMovie(request);

        PersonRequest a = Instancio.create(PersonRequest.class);
        PersonRequest d = Instancio.create(PersonRequest.class);
        Settings settings = Settings.create()
                .set(Keys.STRING_MIN_LENGTH, 1);
        UpdateMovieRequest updateRequest = Instancio.of(UpdateMovieRequest.class)
                .withSettings(settings)
                .set(field(UpdateMovieRequest::actors),new HashSet<>(Set.of(a)))
                .set(field(UpdateMovieRequest::directors),new HashSet<>(Set.of(d)))
                .create();

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(patch("/manager/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateRequest.title()))
                .andExpect(jsonPath("$.synopsis").value(updateRequest.synopsis()))
                .andExpect(jsonPath("$.rating").value(updateRequest.rating().name()))
                .andExpect(jsonPath("$.trailerLink").value(updateRequest.trailerLink()))
                .andExpect(jsonPath("$.movieLink").value(updateRequest.movieLink()))
                .andExpect(jsonPath("$.poster").value(updateRequest.poster()))
                .andExpect(jsonPath("$.yearReleased").value(updateRequest.yearReleased().getValue()))
                .andExpect(jsonPath("$.duration").value(updateRequest.duration()))
                .andExpect(jsonPath("$.actors[0].fullName").value(a.firstName() + " "+a.lastName()))
                .andExpect(jsonPath("$.directors[0].fullName").value(d.firstName() + " "+d.lastName()));
    }

    @Test
    public void deleteCommentFromMovieShouldWork() throws Exception {

        User user = TestUtils.generateUser();
        userRepository.save(user);

        SaveMovieRequest request = Instancio.create(SaveMovieRequest.class);

        movieService.saveMovie(request);

        Movie movie = movieRepository.findByIdWithComments(1)
                .orElseThrow(() -> new MovieNotFoundException(1));

        Comment comment = TestUtils.generateComment();

        comment.setMovie(movie);
        comment.setCreatedBy(user.getRealUserName());

        movie.getComments().add(comment);
        movieRepository.save(movie);

        mockMvc.perform(delete("/manager/comments/1"))
                        .andExpect(status().isOk());
    }


}
