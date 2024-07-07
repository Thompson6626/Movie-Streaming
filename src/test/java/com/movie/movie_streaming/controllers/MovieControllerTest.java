package com.movie.movie_streaming.controllers;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.movie.Genre;
import com.movie.movie_streaming.movie.MovieService;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.MovieResponse;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@AutoConfigureMockMvc
public class MovieControllerTest {
    @MockBean
    private MovieService movieService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieControllerTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }
    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.COLLECTION_MAX_SIZE, 1)
            .set(Keys.COLLECTION_MAX_SIZE, 5)
            .set(Keys.BEAN_VALIDATION_ENABLED,true);

    @Test
    public void homeShouldReturn200() throws Exception {
        when(movieService.findAllMovies(anyInt(),anyInt())).thenReturn(new PageResponse<>());
        mockMvc.perform(get("/movies/home")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }


    @Test
    public void findByMovieByIdShouldReturn200() throws Exception {
        var display = Instancio.create(MovieDisplay.class);
        when(movieService.findMovieById(anyInt())).thenReturn(display);

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(display.getTitle()))
                .andExpect(jsonPath("$.synopsis").value(display.getSynopsis()))
                .andExpect(jsonPath("$.rating").value(display.getRating()))
                .andExpect(jsonPath("$.trailerLink").value(display.getTrailerLink()))
                .andExpect(jsonPath("$.movieLink").value(display.getMovieLink()))
                .andExpect(jsonPath("$.poster").value(display.getPoster()))
                .andExpect(jsonPath("$.yearReleased").value(display.getYearReleased()))
                .andExpect(jsonPath("$.duration").value(display.getDuration()))
                .andExpect(jsonPath("$.genres[*]",containsInAnyOrder(display.getGenres().toArray())))
                .andExpect(jsonPath("$.directors").isArray())
                .andExpect(jsonPath("$.directors[0].fullName").value(display.getDirectors().iterator().next().fullName()))
                .andExpect(jsonPath("$.actors").isArray())
                .andExpect(jsonPath("$.actors[0].fullName").value(display.getActors().iterator().next().fullName()))
                .andExpect(jsonPath("$.comments").isNotEmpty());
    }
    @Test
    public void findingMoviesWithSearchTermShouldWork() throws Exception {
        var movieResponses = Instancio.createList(MovieResponse.class);

        var pageResponses = PageResponse.<MovieResponse>builder()
                .content(movieResponses)
                .build();
        when(movieService.findAllMoviesWithSearchTerm(anyInt(),anyInt(),anyString())).thenReturn(pageResponses);

        mockMvc.perform(get("/movies")
                        .param("s","action")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.content",hasSize(movieResponses.size())));
    }

    @Test
    public void findingMoviesByGenresShouldWork() throws Exception {
        var movieResponses = Instancio.createList(MovieResponse.class);

        var pageResponses = PageResponse.<MovieResponse>builder()
                .content(movieResponses)
                .build();

        when(movieService.findAllMoviesWithGenres(anyInt(),anyInt(),any(Set.class))).thenReturn(pageResponses);

        mockMvc.perform(get("/movies/by-genres")
                        .param("genres", Genre.HORROR.name())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(movieResponses.size())));
    }

    @Test
    public void findingMoviesByActorsShouldWork() throws Exception {
        var movieResponses = Instancio.createList(MovieResponse.class);

        var pageResponses = PageResponse.<MovieResponse>builder()
                .content(movieResponses)
                .build();

        when(movieService.findAllMoviesWithActor(anyInt(),anyInt(),anyInt())).thenReturn(pageResponses);

        mockMvc.perform(get("/movies/actors/1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(movieResponses.size())));
    }
    @Test
    public void findingMoviesByDirectorsShouldWork() throws Exception {
        var movieResponses = Instancio.createList(MovieResponse.class);

        var pageResponses = PageResponse.<MovieResponse>builder()
                .content(movieResponses)
                .build();

        when(movieService.findAllMoviesByDirector(anyInt(),anyInt(),anyInt())).thenReturn(pageResponses);

        mockMvc.perform(get("/movies/directors/1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(movieResponses.size())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void postCommentWorks() throws Exception {
        var postRequest = Instancio.create(PostCommentRequest.class);

        var commentDisplay = Instancio.create(CommentDisplay.class);

        var requestJson = objectMapper.writeValueAsString(postRequest);

        when(movieService.addCommentToMovie(any(PostCommentRequest.class),anyInt())).thenReturn(commentDisplay);
        mockMvc.perform(post("/movies/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated());
    }

}
