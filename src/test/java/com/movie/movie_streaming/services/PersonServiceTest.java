package com.movie.movie_streaming.services;

import com.movie.movie_streaming.actor.Actor;
import com.movie.movie_streaming.actor.ActorRepository;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.director.Director;
import com.movie.movie_streaming.director.DirectorRepository;
import com.movie.movie_streaming.person.PersonMapper;
import com.movie.movie_streaming.person.PersonService;
import com.movie.movie_streaming.person.dto.PersonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    private List<Actor> actorList;
    private List<Director> directorList;
    private Page<Actor> actorPage;
    private Page<Director> directorPage;
    private PageRequest pageRequest;

    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        actorList = List.of(new Actor(), new Actor());
        directorList = List.of(new Director(), new Director());
        actorPage = new PageImpl<>(actorList, pageRequest, actorList.size());
        directorPage = new PageImpl<>(directorList, pageRequest, directorList.size());
    }

    @Test
    public void findAllActorsWith_noFilters() {
        when(actorRepository.findAll(any(Pageable.class))).thenReturn(actorPage);
        when(personMapper.toPersonResponse(any())).thenReturn(new PersonResponse());

        PageResponse<PersonResponse> result = personService.findAllActorsWith(0, 10, null, null);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(actorRepository).findAll(any(Pageable.class));
        verify(personMapper, times(2)).toPersonResponse(any());
    }

    @Test
    public void findAllDirectorsWith_noFilters() {
        when(directorRepository.findAll(any(Pageable.class))).thenReturn(directorPage);
        when(personMapper.toPersonResponse(any())).thenReturn(new PersonResponse());

        PageResponse<PersonResponse> result = personService.findAllDirectorsWith(0, 10, null, null);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(directorRepository).findAll(any(Pageable.class));
        verify(personMapper, times(2)).toPersonResponse(any());
    }

    @Test
    public void findAllActorsWith_filters() {
        when(actorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(actorPage);
        when(personMapper.toPersonResponse(any())).thenReturn(new PersonResponse());

        PageResponse<PersonResponse> result = personService.findAllActorsWith(0, 10, "John", "Doe");

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(actorRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(personMapper, times(2)).toPersonResponse(any());
    }

    @Test
    public void findAllDirectorsWith_filters() {
        when(directorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(directorPage);
        when(personMapper.toPersonResponse(any())).thenReturn(new PersonResponse());

        PageResponse<PersonResponse> result = personService.findAllDirectorsWith(0, 10, "John", "Doe");

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(directorRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(personMapper, times(2)).toPersonResponse(any());
    }
}

