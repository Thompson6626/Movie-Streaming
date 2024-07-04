package com.movie.movie_streaming.person.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String country;
    private String photoLink;
}
