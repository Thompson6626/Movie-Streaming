package com.movie.movie_streaming.person.dto;

import lombok.Builder;

@Builder
public record PersonDisplay(
        Integer id,
        String fullName
) {

}
