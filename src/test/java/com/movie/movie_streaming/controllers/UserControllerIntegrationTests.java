package com.movie.movie_streaming.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.TestUtils;
import com.movie.movie_streaming.movie.MovieService;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import com.movie.movie_streaming.user.dto.ChangePasswordRequest;
import com.movie.movie_streaming.user.dto.UpdateEmailRequest;
import com.movie.movie_streaming.user.dto.UpdateUsernameRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    private final UserRepository userRepository;
    private final MovieService movieService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserControllerIntegrationTests(
            UserRepository userRepository,
            MovieService movieService,
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.movieService = movieService;
    }

    @Test
    public void changingPasswordShouldWork() throws Exception {

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = TestUtils.generateUser();

        user = userRepository.save(user);

        Authentication authentication = TestUtils.setAuthentication(user);

        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPassword("password1")
                .newPassword("newPassword")
                .confirmationPassword("newPassword")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .principal(authentication)
                )
                .andExpect(status().isOk());

        Optional<User> found = userRepository.findById(1);

        assertThat(found)
                .isPresent()
                .get()
                .extracting(User::getPassword)
                .asString()
                .matches(e -> encoder.matches("newPassword",e));


        SecurityContextHolder.clearContext();
    }

    @Test
    public void accessFavouritesAsAnonymousShouldReturn403() throws Exception {
        mockMvc.perform(get("/users/favourites")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }
    @Test
    public void accessFavouritesAsUserShouldReturn200() throws Exception {
        User user = TestUtils.generateUser();

        user = userRepository.save(user);
        movieService.saveMovie(Instancio.create(SaveMovieRequest.class));

        Authentication authentication = TestUtils.setAuthentication(user);

        mockMvc.perform(post("/users/favourites/1")
                .principal(authentication)
        ).andExpect(status().isOk());

        mockMvc.perform(get("/users/favourites")
                        .principal(authentication)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content", hasSize(1)));

        SecurityContextHolder.clearContext();
    }

    @Test
    public void accessFavouritesShouldReturn403() throws Exception {
        mockMvc.perform(get("/users/favourites")
        ).andExpect(status().isForbidden());
    }

    @Test
    public void changingUsernameShouldWork() throws Exception {

        User user = TestUtils.generateUser();

        user = userRepository.save(user);

        Authentication authentication = TestUtils.setAuthentication(user);

        UpdateUsernameRequest request = new UpdateUsernameRequest("MyNewUsername");
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/users/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                .principal(authentication)
        ).andExpect(status().isOk());

        Optional<User> found = userRepository.findById(1);
        assertThat(found)
                .isPresent()
                .get()
                .extracting(User::getRealUserName)
                .isEqualTo("MyNewUsername");
    }

    @Test
    public void changingEmailShouldWork() throws Exception {

        User user = TestUtils.generateUser();

        user = userRepository.save(user);

        Authentication authentication = TestUtils.setAuthentication(user);
        UpdateEmailRequest request = new UpdateEmailRequest("myNewEmail@gmail.com");
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/users/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                .principal(authentication)
        ).andExpect(status().isOk());

        Optional<User> found = userRepository.findById(1);
        assertThat(found)
                .isPresent()
                .get()
                .extracting(User::getEmail)
                .isEqualTo("myNewEmail@gmail.com");
    }
}
