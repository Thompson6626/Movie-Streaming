package com.movie.movie_streaming.controllers;


import com.movie.movie_streaming.actor.Actor;
import com.movie.movie_streaming.actor.ActorRepository;
import com.movie.movie_streaming.director.Director;
import com.movie.movie_streaming.director.DirectorRepository;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PersonControllerIntegrationTests {


    private final MockMvc mockMvc;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    @Autowired
    public PersonControllerIntegrationTests(
            MockMvc mockMvc,
            ActorRepository actorRepository,
            DirectorRepository directorRepository
    ) {
        this.mockMvc = mockMvc;
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
    }

    @Test
    public void searchActorsByFirstNameShouldReturn1Actor() throws Exception {
        Actor actor = Actor.builder()
                .firstName("Thomas")
                .lastName("Benjamim")
                .country("United States")
                .birthDate(LocalDate.of(2004,6,2))
                .build();
        actorRepository.save(actor);

        mockMvc.perform(get("/actors")
                        .param("first-name","thoMas"))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content[0].firstName").value(actor.getFirstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(actor.getLastName()));
    }

    @Test
    public void searchDirectorsByFirstNameShouldReturn1Director() throws Exception {
        Director director = Director.builder()
                .firstName("David")
                .lastName("Brown")
                .country("United States")
                .birthDate(LocalDate.of(2004,6,2))
                .build();
        directorRepository.save(director);

        mockMvc.perform(get("/directors")
                        .param("first-name","brown"))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content[0].firstName").value(director.getFirstName()))
                .andExpect(jsonPath("$.content[0].lastName").value(director.getLastName()));
    }


}
