package com.movie.movie_streaming.movie;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Genre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    FOREIGN("Foreign"),
    HORROR("Horror"),
    MUSICAL("Musical"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi"),
    THRILLER("Thriller"),
    WESTERN("Western"),
    OTHER("Other");

    private final String description;

    public static Genre fromString(String genreStr) {
        String enumName = genreStr.replaceAll("-", "_").toUpperCase();
        return Genre.valueOf(enumName);
    }
    public static Set<String> toStringSet(Set<Genre> genreSet){
        return genreSet.stream()
                .map(Genre::getDescription)
                .collect(Collectors.toSet());
    }
}