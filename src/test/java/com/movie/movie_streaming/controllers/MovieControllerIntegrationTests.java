package com.movie.movie_streaming.controllers;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.TestUtils;
import com.movie.movie_streaming.comment.dto.PostCommentRequest;
import com.movie.movie_streaming.movie.Genre;
import com.movie.movie_streaming.movie.MovieServiceImpl;
import com.movie.movie_streaming.movie.dto.MovieDisplay;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.person.dto.PersonRequest;
import com.movie.movie_streaming.user.User;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MovieControllerIntegrationTests {
    private final MovieServiceImpl movieService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieControllerIntegrationTests(
            MovieServiceImpl movieService,
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ) {
        this.movieService = movieService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }
    @WithSettings
    private final Settings settings = Settings.create()
            .set(Keys.COLLECTION_MAX_SIZE, 1)
            .set(Keys.COLLECTION_MAX_SIZE, 5)
            .set(Keys.BEAN_VALIDATION_ENABLED,true);
    @Test
    @WithMockUser(roles = "USER")
    public void accessSecuredEndpointWithoutPermissionShouldFail() throws Exception {
        mockMvc.perform(post("/manager/movies"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void homeShouldReturn200() throws Exception {
        mockMvc.perform(get("/movies/home")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "USER")
    public void findByMovieByIdShouldReturn200() throws Exception {
        SaveMovieRequest request = Instancio.create(SaveMovieRequest.class);
        movieService.saveMovie(request);

        mockMvc.perform(get("/movies/1")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").value(request.title()));
    }
    @Test
    public void findingMoviesWithSearchTermShouldWork() throws Exception {
        SaveMovieRequest req1 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::title),"Action movie")
                .create();
        SaveMovieRequest req2 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::title),"MORE ACTION")
                .create();
        SaveMovieRequest req3 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::title),"Another normal movie")
                .create();
        movieService.saveMovie(req1);
        movieService.saveMovie(req2);
        movieService.saveMovie(req3);

        mockMvc.perform(get("/movies")
                        .param("s","action")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content",hasSize(2)));
    }

    @Test
    public void findingMoviesByGenresShouldWork() throws Exception {
        SaveMovieRequest req1 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::genres),new HashSet<>(Set.of(Genre.MYSTERY,Genre.HORROR)))
                .create();
        SaveMovieRequest req2 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::genres), new HashSet<>(Set.of(Genre.THRILLER,Genre.ACTION)))
                .create();
        SaveMovieRequest req3 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::genres),new HashSet<>(Set.of(Genre.HORROR)))
                .create();
        movieService.saveMovie(req1);
        movieService.saveMovie(req2);
        movieService.saveMovie(req3);

        mockMvc.perform(get("/movies/by-genres")
                        .param("genres", Genre.HORROR.name())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content",hasSize(2)));
    }

    @Test
    public void findingMoviesByActorsShouldWork() throws Exception {
        PersonRequest pq1 = Instancio.create(PersonRequest.class);
        PersonRequest pq2 = Instancio.create(PersonRequest.class);
        PersonRequest pq3 = Instancio.create(PersonRequest.class);
        PersonRequest pq4 = Instancio.create(PersonRequest.class);
        // Person request 3 is saved first
        SaveMovieRequest req1 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::actors),new HashSet<>(Set.of(pq3)))
                .create();
        SaveMovieRequest req2 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::actors), new HashSet<>(Set.of(pq4,pq1,pq3)))
                .create();
        SaveMovieRequest req3 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::actors),new HashSet<>(Set.of(pq2,pq3,pq4)))
                .create();
        movieService.saveMovie(req1);
        movieService.saveMovie(req2);
        movieService.saveMovie(req3);

        mockMvc.perform(get("/movies/actors/1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content",hasSize(3)));
    }
    @Test
    public void findingMoviesByDirectorsShouldWork() throws Exception {
        PersonRequest pq1 = Instancio.create(PersonRequest.class);
        PersonRequest pq2 = Instancio.create(PersonRequest.class);
        PersonRequest pq3 = Instancio.create(PersonRequest.class);
        PersonRequest pq4 = Instancio.create(PersonRequest.class);
        // Person request 3 is saved first
        SaveMovieRequest req1 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::directors),new HashSet<>(Set.of(pq3)))
                .create();
        SaveMovieRequest req2 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::directors), new HashSet<>(Set.of(pq4,pq1,pq3)))
                .create();
        SaveMovieRequest req3 = Instancio.of(SaveMovieRequest.class)
                .set(field(SaveMovieRequest::directors),new HashSet<>(Set.of(pq2,pq3,pq4)))
                .create();
        movieService.saveMovie(req1);
        movieService.saveMovie(req2);
        movieService.saveMovie(req3);

        mockMvc.perform(get("/movies/directors/1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content",hasSize(3)));
    }

    @Test
    public void postCommentWorks() throws Exception {
        User user = TestUtils.generateUser();
        Authentication authentication = TestUtils.setAuthentication(user);

        SaveMovieRequest saveRequest = Instancio.create(SaveMovieRequest.class);
        movieService.saveMovie(saveRequest);

        PostCommentRequest request = Instancio.create(PostCommentRequest.class);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/movies/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .principal(authentication)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        MovieDisplay display = movieService.findMovieById(1);
        assertEquals(1,display.getComments().size());

        SecurityContextHolder.clearContext();
    }

}
