package com.movie.movie_streaming.repositories;



import com.movie.movie_streaming.actor.Actor;
import com.movie.movie_streaming.actor.ActorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ActorRepositoryTest {
    @MockBean
    private AuditingHandler auditingHandler;
    @Autowired
    private ActorRepository actorRepository;
    @Test
    void correctFindOneUniqueActor(){
        Actor actor = Actor.builder()
                .firstName("Benjamin")
                .lastName("Thomas")
                .country("United States")
                .birthDate(LocalDate.of(2013,7,20))
                .build();

        actorRepository.save(actor);

        Optional<Actor> uniqueActor = actorRepository.findUniqueActor(
                "Benjamin","Thomas","United States",LocalDate.of(2013,7,20));

        assertThat(uniqueActor)
                .isPresent()
                .get()
                .satisfies(act -> {
                    assertEquals(actor.getFirstName(), act.getFirstName());
                    assertEquals(actor.getLastName(), act.getLastName());
                    assertEquals(actor.getCountry(), act.getCountry());
                    assertEquals(actor.getBirthDate(), act.getBirthDate());
                });
    }
}
