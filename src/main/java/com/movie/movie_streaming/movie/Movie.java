package com.movie.movie_streaming.movie;


import com.movie.movie_streaming.actor.Actor;
import com.movie.movie_streaming.comment.Comment;
import com.movie.movie_streaming.director.Director;
import jakarta.persistence.*;
import lombok.*;

import java.beans.Transient;
import java.time.Year;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
@EqualsAndHashCode
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @Column(nullable = false)
    private String title;
    private String synopsis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rating rating;

    private String poster;
    private String trailerLink;
    private String movieLink;

    @Column(nullable = false)
    private Year yearReleased;

    private Integer duration;


    @ElementCollection(targetClass = Genre.class,fetch = FetchType.EAGER)
    @JoinTable(name = "movie_genres", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres;

    @ManyToMany(
            mappedBy = "moviesDirected",
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @EqualsAndHashCode.Exclude
    private Set<Director> directors;

    @ManyToMany(
            mappedBy = "moviesActed",
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @EqualsAndHashCode.Exclude
    private Set<Actor> actors;
    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @EqualsAndHashCode.Exclude
    private List<Comment> comments;

    @PreRemove
    public void removeActorAssociations(){
        this.actors.forEach(a -> a.getMoviesInternal().remove(this));
        this.directors.forEach(d -> d.getMoviesInternal().remove(this));
    }
    @Transient
    public double getAvgCommentStars(){
        if (comments == null || comments.isEmpty()) {
            return 0.0;
        }

        var rating = comments.stream()
                .mapToDouble(Comment::getStars)
                .average()
                .orElse(0.0);

        var roundedRate = Math.round(rating * 10.0) / 10.0;

        return roundedRate;
    }

}
