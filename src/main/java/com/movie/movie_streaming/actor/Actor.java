package com.movie.movie_streaming.actor;


import com.movie.movie_streaming.movie.Movie;
import com.movie.movie_streaming.person.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;


@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "actors")
@EqualsAndHashCode(callSuper = true)
public class Actor extends Person {

    @ManyToMany
    @JoinTable(
            name = "Actor_Movie",
            joinColumns = { @JoinColumn(name = "actor_id") },
            inverseJoinColumns = { @JoinColumn(name = "movie_id") }
    )
    @EqualsAndHashCode.Exclude
    Set<Movie> moviesActed;

    public Set<Movie> getMoviesInternal() {
        if (moviesActed == null) {
            moviesActed = new HashSet<>();
        }
        return moviesActed;
    }
    public void addMovieActed(Movie movie){
        getMoviesInternal().add(movie);
    }
}
