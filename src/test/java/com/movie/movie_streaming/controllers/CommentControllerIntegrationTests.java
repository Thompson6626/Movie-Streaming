package com.movie.movie_streaming.controllers;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.TestUtils;
import com.movie.movie_streaming.comment.Comment;
import com.movie.movie_streaming.comment.dto.UpdateCommentRequest;
import com.movie.movie_streaming.exceptions.MovieNotFoundException;
import com.movie.movie_streaming.movie.Movie;
import com.movie.movie_streaming.movie.MovieRepository;
import com.movie.movie_streaming.movie.MovieService;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.user.User;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CommentControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final MovieService movieService;
    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentControllerIntegrationTests(
            MockMvc mockMvc,
            MovieService movieService,
            MovieRepository movieRepository,
            ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
    }

    @Test
    public void updatingCommentShouldWork() throws Exception {
        User user = TestUtils.generateUser();

        Authentication authentication = TestUtils.setAuthentication(user);

        SaveMovieRequest request = Instancio.create(SaveMovieRequest.class);
        movieService.saveMovie(request);

        Movie movie = movieRepository.findById(1)
                .orElseThrow(() -> new MovieNotFoundException(1));

        Comment comment = Comment.builder()
                .stars(5.0)
                .body("This is a great movie")
                .movie(movie)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .createdBy(user.getRealUserName())
                .build();
        movie.getComments().add(comment);
        movie = movieRepository.save(movie);
        comment = movie.getComments().get(0);

        LocalDateTime createdDate = comment.getCreatedDate();
        LocalDateTime prevLastModified = comment.getLastModifiedDate();


        UpdateCommentRequest updateRequest = new UpdateCommentRequest(1,"This a GREAT movie!!");
        String jsonReq = objectMapper.writeValueAsString(updateRequest);


        mockMvc.perform(patch("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReq)
                .principal(authentication)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.body").value(updateRequest.body()))
                .andExpect(jsonPath("$.stars").value(comment.getStars()))
                .andExpect(jsonPath("$.createdBy").value(comment.getCreatedBy()));

        SecurityContextHolder.clearContext();


        movie = movieRepository.findById(1)
                .orElseThrow(() -> new MovieNotFoundException(1));

        comment = movie.getComments().get(0);
        assertThat(comment.getCreatedDate()).isCloseTo(createdDate,within(1,ChronoUnit.MILLIS));
        assertThat(comment.getLastModifiedDate()).isAfter(prevLastModified);

    }
}
