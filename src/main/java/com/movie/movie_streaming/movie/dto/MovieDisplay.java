package com.movie.movie_streaming.movie.dto;



import com.movie.movie_streaming.comment.dto.CommentDisplay;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.person.dto.PersonDisplay;
import lombok.*;


import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDisplay {

    private String title;
    private String synopsis;

    private String rating;

    private String trailerLink;
    private String movieLink;
    private String poster;

    private Integer yearReleased;

    private Integer duration;

    private Set<String> genres;
    private Set<PersonDisplay> directors;
    private Set<PersonDisplay> actors;
    private PageResponse<CommentDisplay> comments;
}
