package com.movie.movie_streaming.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.comment.CommentService;
import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.comment.dto.UpdateCommentRequest;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private final MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    public CommentControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updatingCommentShouldWork() throws Exception {
        CommentDisplay display = Instancio.create(CommentDisplay.class);
        UpdateCommentRequest updateRequest = Instancio.create(UpdateCommentRequest.class);

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        when(commentService.updateComment(any(UpdateCommentRequest.class),anyInt(),any(Authentication.class))).thenReturn(display);
        mockMvc.perform(patch("/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(display.getId()))
                .andExpect(jsonPath("$.stars").value(display.getStars()))
                .andExpect(jsonPath("$.body").value(display.getBody()))
                .andExpect(jsonPath("$.createdBy").value(display.getCreatedBy()))
                .andExpect(jsonPath("$.postedAt").value(display.getPostedAt().toString()))
                .andExpect(jsonPath("$.lastModifiedAt").value(display.getLastModifiedAt().toString()));
    }
}
