package com.movie.movie_streaming.mappers;


import com.movie.movie_streaming.actor.Actor;
import com.movie.movie_streaming.director.Director;
import com.movie.movie_streaming.person.PersonMapper;
import com.movie.movie_streaming.person.dto.PersonRequest;
import com.movie.movie_streaming.person.dto.PersonResponse;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(InstancioExtension.class)
public class PersonMapperTest {
    private PersonMapper personMapper;

    @BeforeEach
    public void init(){
        personMapper = new PersonMapper();
    }

    @Test
    void correctPersonRequestToActor(){
        PersonRequest request = Instancio.of(PersonRequest.class)
                .withNullable(field(PersonRequest::photoLink))
                .create();
        Actor actor = personMapper.toActor(request);
        assertEquals(actor.getFirstName(),request.firstName());
        assertEquals(actor.getLastName(),request.lastName());
        assertEquals(actor.getCountry(),request.country());
        assertEquals(actor.getBirthDate(),request.birthDate());
        assertEquals(actor.getPhotoLink(),request.photoLink());
    }
    @Test
    void correctPersonRequestToDirector(){
        PersonRequest request = Instancio.of(PersonRequest.class)
                .withNullable(field(PersonRequest::photoLink))
                .create();
        Director actor = personMapper.toDirector(request);
        assertEquals(actor.getFirstName(),request.firstName());
        assertEquals(actor.getLastName(),request.lastName());
        assertEquals(actor.getCountry(),request.country());
        assertEquals(actor.getBirthDate(),request.birthDate());
        assertEquals(actor.getPhotoLink(),request.photoLink());
    }

    @Test
    void correctActortoPersonRequest() {
        Actor actor = Instancio.create(Actor.class);

        PersonResponse response = personMapper.toPersonResponse(actor);
        assertEquals(actor.getId(), response.getId());
        assertEquals(actor.getFirstName(),response.getFirstName());
        assertEquals(actor.getLastName(),response.getLastName());
        assertEquals(actor.getCountry(),response.getCountry());
        assertEquals(actor.getPhotoLink(),response.getPhotoLink());
    }
    @Test
    void correctDirectortoPersonRequest() {
        Director actor = Instancio.create(Director.class);

        PersonResponse response = personMapper.toPersonResponse(actor);
        assertEquals(actor.getId(), response.getId());
        assertEquals(actor.getFirstName(),response.getFirstName());
        assertEquals(actor.getLastName(),response.getLastName());
        assertEquals(actor.getCountry(),response.getCountry());
        assertEquals(actor.getPhotoLink(),response.getPhotoLink());
    }
}
