package com.movie.movie_streaming.person;


import com.movie.movie_streaming.actor.Actor;
import com.movie.movie_streaming.director.Director;
import com.movie.movie_streaming.person.dto.PersonRequest;
import com.movie.movie_streaming.person.dto.PersonResponse;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
public class PersonMapper {
    public Actor toActor(PersonRequest request) {
        return toEntity(request, Actor::new);
    }

    public Director toDirector(PersonRequest request) {
        return toEntity(request, Director::new);
    }

    public <T extends Person> T toEntity(PersonRequest request, Supplier<T> supplier) {
        T entity = supplier.get();
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setCountry(request.country());
        entity.setBirthDate(request.birthDate());
        entity.setPhotoLink(request.photoLink());
        return entity;
    }
    public Set<Actor> toActorSet(Set<PersonRequest> requests){
        return toEntitySet(requests,this::toActor);
    }
    public Set<Director> toDirectorSet(Set<PersonRequest> requests){
        return toEntitySet(requests,this::toDirector);
    }
    private <T extends Person> Set<T> toEntitySet(Set<PersonRequest> requests, Function<PersonRequest, T> mapper) {
        if (requests == null) return null;

        return requests.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
    public <T extends Person> PersonResponse toPersonResponse(T entity){
        return PersonResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .country(entity.getCountry())
                .photoLink(entity.getPhotoLink())
                .build();
    }



}
