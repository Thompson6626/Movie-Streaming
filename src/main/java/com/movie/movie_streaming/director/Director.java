package com.movie.movie_streaming.director;


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
@Table(name = "directors")
@EqualsAndHashCode(callSuper = true)
public class Director extends Person {

    @ManyToMany
    @JoinTable(
            name = "Director_Movie",
            joinColumns = { @JoinColumn(name = "director_id") },
            inverseJoinColumns = { @JoinColumn(name = "movie_id") }
    )
    @EqualsAndHashCode.Exclude
    Set<Movie> moviesDirected;

    public Set<Movie> getMoviesInternal() {
        if (moviesDirected == null){
            moviesDirected = new HashSet<>();
        }
        return moviesDirected;
    }
    public void addMovieDirected(Movie movie){
        getMoviesInternal().add(movie);
    }
}
