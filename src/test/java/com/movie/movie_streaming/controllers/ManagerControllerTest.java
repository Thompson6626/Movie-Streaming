package com.movie.movie_streaming.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.comment.CommentService;
import com.movie.movie_streaming.movie.MovieService;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.movie.dto.UpdateMovieRequest;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@AutoConfigureMockMvc
@WithMockUser(roles = "MANAGER")
public class ManagerControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private MovieService movieService;
    @MockBean
    private CommentService commentService;
    @Autowired
    public ManagerControllerTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }
    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.COLLECTION_MAX_SIZE, 1)
            .set(Keys.BEAN_VALIDATION_ENABLED,true)
            .set(Keys.STRING_MAX_LENGTH,20);

    @Test
    public void movieShouldBeCreated() throws Exception {

        var saveRequest = Instancio.create(SaveMovieRequest.class);
        var display = Instancio.create(MovieDisplay.class);

        when(movieService.saveMovie(any(SaveMovieRequest.class))).thenReturn(display);
        var jsonRequest = objectMapper.writeValueAsString(saveRequest);

        mockMvc.perform(post("/manager/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
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
    public void deleteMovieShouldWork() throws Exception {
        mockMvc.perform(delete("/manager/movies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateMovieShouldWork() throws Exception {
        var display = Instancio.create(MovieDisplay.class);

        when(movieService.updateMovie(anyInt(),any(UpdateMovieRequest.class))).thenReturn(display);

        var updateRequest = Instancio.create(UpdateMovieRequest.class);
        var jsonRequest = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(patch("/manager/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
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
    public void deleteCommentFromMovieShouldWork() throws Exception {
        mockMvc.perform(delete("/manager/comments/1"))
                .andExpect(status().isNoContent());
    }


}
