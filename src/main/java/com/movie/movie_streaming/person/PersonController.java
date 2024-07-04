package com.movie.movie_streaming.person;


import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.person.dto.PersonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    @GetMapping("/actors")
    public ResponseEntity<PageResponse<PersonResponse>> findAllActorsWith(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "first-name", required = false) String firstName,
            @RequestParam(name = "last-name", required = false) String lastName
    ){
        return ResponseEntity.ok(personService.findAllActorsWith(page, size,firstName,lastName));
    }

    @GetMapping("/directors")
    public ResponseEntity<PageResponse<PersonResponse>> findAllDirectorsWith(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "first-name", required = false) String firstName,
            @RequestParam(name = "last-name", required = false) String lastName
    ){
        return ResponseEntity.ok(personService.findAllDirectorsWith(page, size,firstName,lastName));
    }




}
