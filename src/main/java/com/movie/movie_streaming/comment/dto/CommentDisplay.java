package com.movie.movie_streaming.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDisplay {

    private Integer id;
    private Double stars;
    private String body;

    private LocalDateTime postedAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
}
