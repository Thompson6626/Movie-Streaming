package com.movie.movie_streaming.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rating {
    G("General Audiences - All Ages"),
    PG("Parental Guidance Suggested - Some material may not be suitable for children."),
    PG_13("Parents Strongly Cautioned - Some material may be inappropriate for children under 13."),
    R("Restricted - Under 17 requires accompanying parent or adult guardian."),
    NC_17("No Children Under 17 - Adults Only."),
    NR("Not Rated");

    private final String description;
}