package com.movie.movie_streaming.movie.dto;

import com.movie.movie_streaming.movie.Rating;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {

    private Integer id;
    private String title;
    private Rating rating;
    private Double commentAvgRating;
    private Integer yearReleased;
    // Minutes
    private Integer duration;
    private String poster;
}
