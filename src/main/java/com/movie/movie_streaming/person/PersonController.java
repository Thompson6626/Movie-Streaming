package com.movie.movie_streaming.person;


import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.person.dto.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Person")
public class PersonController {

    private final PersonService personService;
    @Operation(
            summary = "Find all actors",
            description = "Returns a page response of actors filtered by the provided first name and/or last name or all the actors if there's no first name or last name",
            responses = {
                    @ApiResponse(
                            description = "Successful",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/actors")
    public ResponseEntity<PageResponse<PersonResponse>> findAllActorsWith(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "first-name", required = false) String firstName,
            @RequestParam(name = "last-name", required = false) String lastName
    ){
        return ResponseEntity.ok(personService.findAllActorsWith(page, size,firstName,lastName));
    }
    @Operation(
            summary = "Find all directors",
            description = "Returns a page response of directors filtered by the provided first name and/or last name or all the directors if there's no first name or last name",
            responses = {
                    @ApiResponse(
                            description = "Successful",
                            responseCode = "200"
                    )
            }
    )
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
