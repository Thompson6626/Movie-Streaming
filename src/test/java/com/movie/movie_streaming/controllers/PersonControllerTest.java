package com.movie.movie_streaming.controllers;


import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.person.PersonService;
import com.movie.movie_streaming.person.dto.PersonResponse;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(InstancioExtension.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersonService personService;


    @Test
    public void findingActorsByNamesShouldWork() throws Exception {
        var personResponses = Instancio.createList(PersonResponse.class);

        var pageResponse = PageResponse.<PersonResponse>builder()
                .content(personResponses)
                .build();

        when(personService.findAllActorsWith(anyInt(),anyInt(),nullable(String.class),nullable(String.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/actors")
                        .param("first-name","thoMas"))
                .andExpect(jsonPath("$.content",hasSize(personResponses.size())));
    }

    @Test
    public void findingDirectorsByNamesShouldWork() throws Exception {
        var personResponses = Instancio.createList(PersonResponse.class);

        var pageResponse = PageResponse.<PersonResponse>builder()
                .content(personResponses)
                .build();

        when(personService.findAllDirectorsWith(anyInt(),anyInt(),nullable(String.class),nullable(String.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/directors")
                        .param("first-name","brown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(personResponses.size())));
    }


}
