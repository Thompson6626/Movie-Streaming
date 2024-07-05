package com.movie.movie_streaming.repositories;


import com.movie.movie_streaming.director.Director;
import com.movie.movie_streaming.director.DirectorRepository;
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
public class DirectorRepositoryTest {
    @MockBean
    private AuditingHandler auditingHandler;
    @Autowired
    private DirectorRepository directorRepository;
    @Test
    void correctFindOneUniqueActor(){
        Director director = Director.builder()
                .firstName("Charles")
                .lastName("Gabriel")
                .country("United States")
                .birthDate(LocalDate.of(2009,3,12))
                .build();
        directorRepository.save(director);

        Optional<Director> uniqueDirector = directorRepository.findUniqueDirector(
                "Charles",
                "Gabriel",
                "United States",
                LocalDate.of(2009,3,12)
        );

        assertThat(uniqueDirector)
                .isPresent()
                .get()
                .satisfies(dir -> {
                    assertEquals("Charles", dir.getFirstName());
                    assertEquals("Gabriel" , dir.getLastName());
                    assertEquals("United States",dir.getCountry());
                    assertEquals(LocalDate.of(2009,3,12), dir.getBirthDate());
                });
    }
}
