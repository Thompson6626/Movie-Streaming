package com.movie.movie_streaming.comment;



import com.movie.movie_streaming.common.BaseEntity;
import com.movie.movie_streaming.movie.Movie;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Comment extends BaseEntity {

    private Double stars;
    private String body;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "movie_id")
    private Movie movie;

}
