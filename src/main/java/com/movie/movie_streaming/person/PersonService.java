package com.movie.movie_streaming.person;

import com.movie.movie_streaming.Utilities.Utils;
import com.movie.movie_streaming.actor.ActorRepository;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.director.DirectorRepository;
import com.movie.movie_streaming.person.dto.PersonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final PersonMapper personMapper;
    public PageResponse<PersonResponse> findAllActorsWith(int page, int size, String firstName, String lastName) {
        return findAllWith(actorRepository,page,size,firstName,lastName);
    }
    public PageResponse<PersonResponse> findAllDirectorsWith(int page, int size, String firstName, String lastName) {
        return findAllWith(directorRepository,page,size,firstName,lastName);
    }


    private <T extends Person,R extends JpaRepository<T,Integer> & JpaSpecificationExecutor<T>> PageResponse<PersonResponse> findAllWith(
            R repository,
            int page,
            int size,
            String firstName, String lastName
    ) {
        Pageable pageable = PageRequest.of(page,size);
        Page<T> people;
        if (firstName == null && lastName == null){
            people = repository.findAll(pageable);
        }else{
            Specification<T> spec = buildSpecification(firstName, lastName);
            people = repository.findAll(spec, pageable);
        }
        return Utils.generatePageResponse(people,personMapper::toPersonResponse);
    }

    private <T extends Person> Specification<T> buildSpecification(String firstName, String lastName) {
        Specification<T> spec;
        if (firstName != null && lastName != null) {
            spec = PersonSpecifications.hasFullName(firstName.trim(), lastName.trim());
        } else {
            String name = (firstName == null ? lastName : firstName).trim();
            spec = PersonSpecifications.<T>hasFirstName(name)
                    .or(PersonSpecifications.hasLastName(name));
        }
        return spec;
    }


}
