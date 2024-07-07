package com.movie.movie_streaming.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.user.UserService;
import com.movie.movie_streaming.user.dto.ChangePasswordRequest;
import com.movie.movie_streaming.user.dto.UpdateEmailRequest;
import com.movie.movie_streaming.user.dto.UpdateUsernameRequest;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(InstancioExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Autowired
    public UserControllerTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @WithMockUser(roles = "USER")
    public void changingPasswordShouldWork() throws Exception {
        var changeRequest = Instancio.create(ChangePasswordRequest.class);
        var jsonRequest = objectMapper.writeValueAsString(changeRequest);

        mockMvc.perform(patch("/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void nonAuthenticatedShouldntAccessUserEndpoint() throws Exception {
        mockMvc.perform(get("/users/favourites"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "USER")
    public void accessFavouritesAsUserShouldReturn200() throws Exception {
        var movieResponses = Instancio.createList(MovieResponse.class);

        var pageResponse = PageResponse.<MovieResponse>builder()
                .content(movieResponses)
                .build();

        when(userService.findAllFavourites(anyInt(),anyInt(),any(Authentication.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/users/favourites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(movieResponses.size())));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addingMovieToFavouritesEndpointShouldWork() throws Exception {
        mockMvc.perform(post("/users/favourites/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deletingMovieToFavouritesEndpointShouldWork() throws Exception {
        mockMvc.perform(delete("/users/favourites/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(roles = "USER")
    public void changingUsernameShouldWork() throws Exception {
        var updateRequest = Instancio.create(UpdateUsernameRequest.class);
        var requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(patch("/users/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void changingEmailShouldWork() throws Exception {
        var updateRequest = Instancio.create(UpdateEmailRequest.class);
        var requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(patch("/users/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNoContent());
    }
}
