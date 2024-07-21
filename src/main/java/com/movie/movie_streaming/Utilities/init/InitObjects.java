package com.movie.movie_streaming.Utilities.init;

import com.movie.movie_streaming.movie.Genre;
import com.movie.movie_streaming.movie.Rating;
import com.movie.movie_streaming.movie.dto.SaveMovieRequest;
import com.movie.movie_streaming.person.dto.PersonRequest;
import com.movie.movie_streaming.role.Role;
import com.movie.movie_streaming.user.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitObjects {

    public static User admin(){
        return User.builder()
                .username("admin")
                .email("adminemail@gmail.com")
                .password(new BCryptPasswordEncoder().encode("password1"))
                .createdDate(LocalDateTime.now())
                .accountLocked(false)
                .enabled(true)
                .role(Role.ADMIN)
                .build();
    }

    private static final PersonRequest actor1 = PersonRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .country("USA")
            .birthDate(LocalDate.of(1980, 5, 15))
            .photoLink("http://example.com/johndoe.jpg")
            .build();

    private static final PersonRequest actor2 = PersonRequest.builder()
            .firstName("Jane")
            .lastName("Smith")
            .country("UK")
            .birthDate(LocalDate.of(1985, 8, 22))
            .photoLink("http://example.com/janesmith.jpg")
            .build();

    private static final PersonRequest director1 = PersonRequest.builder()
            .firstName("Robert")
            .lastName("Brown")
            .country("Canada")
            .birthDate(LocalDate.of(1975, 11, 30))
            .photoLink("http://example.com/robertbrown.jpg")
            .build();

    private static final PersonRequest director2 = PersonRequest.builder()
            .firstName("Emily")
            .lastName("Johnson")
            .country("Australia")
            .birthDate(LocalDate.of(1990, 2, 12))
            .photoLink("http://example.com/emilyjohnson.jpg")
            .build();

    public static List<SaveMovieRequest> movies(){
        return new ArrayList<>(List.of(
                SaveMovieRequest.builder()
                        .title("Sci-Fi Chronicles")
                        .synopsis("A futuristic tale set in a dystopian world with advanced technology.")
                        .rating(Rating.NC_17)
                        .trailerLink("http://example.com/trailer5.mp4")
                        .movieLink("http://example.com/movie5.mp4")
                        .poster("http://example.com/poster5.jpg")
                        .yearReleased(Year.of(2023))
                        .duration(150)
                        .genres(new HashSet<>(Set.of(Genre.SCI_FI, Genre.FANTASY)))
                        .directors(new HashSet<>(Set.of(director1, director2)))
                        .actors(new HashSet<>(Set.of(actor1, actor2)))
                        .build(),
                SaveMovieRequest.builder()
                        .title("Comedic Escapade")
                        .synopsis("A hilarious journey of a group of friends on a wild adventure.")
                        .rating(Rating.G)
                        .trailerLink("http://example.com/trailer4.mp4")
                        .movieLink("http://example.com/movie4.mp4")
                        .poster("http://example.com/poster4.jpg")
                        .yearReleased(Year.of(2024))
                        .duration(100)
                        .genres(new HashSet<>(Set.of(Genre.COMEDY, Genre.FAMILY)))
                        .directors(new HashSet<>(Set.of(director2)))
                        .actors(new HashSet<>(Set.of(actor2)))
                        .build(),
                SaveMovieRequest.builder()
                        .title("Mystery Solved")
                        .synopsis("A detective unravels a complex case with surprising revelations.")
                        .rating(Rating.PG)
                        .trailerLink("http://example.com/trailer3.mp4")
                        .movieLink("http://example.com/movie3.mp4")
                        .poster("http://example.com/poster3.jpg")
                        .yearReleased(Year.of(2025))
                        .duration(130)
                        .genres(new HashSet<>(Set.of(Genre.MYSTERY, Genre.THRILLER)))
                        .directors(new HashSet<>(Set.of(director1)))
                        .actors(new HashSet<>(Set.of(actor1)))
                        .build(),
                SaveMovieRequest.builder()
                        .title("Heartfelt Romance")
                        .synopsis("A story of two souls finding each other amidst life's trials.")
                        .rating(Rating.PG_13)
                        .trailerLink("http://example.com/trailer2.mp4")
                        .movieLink("http://example.com/movie2.mp4")
                        .poster("http://example.com/poster2.jpg")
                        .yearReleased(Year.of(2023))
                        .duration(120)
                        .genres(new HashSet<>(Set.of(Genre.ROMANCE, Genre.DRAMA)))
                        .directors(new HashSet<>(Set.of(director2)))
                        .actors(new HashSet<>(Set.of(actor2)))
                        .build(),
                SaveMovieRequest.builder()
                        .title("Epic Battle")
                        .synopsis("An epic battle unfolds between two ancient civilizations.")
                        .rating(Rating.R)
                        .trailerLink("http://example.com/trailer1.mp4")
                        .movieLink("http://example.com/movie1.mp4")
                        .poster("http://example.com/poster1.jpg")
                        .yearReleased(Year.of(2024))
                        .duration(140)
                        .genres(new HashSet<>(Set.of(Genre.ACTION, Genre.ADVENTURE)))
                        .directors(new HashSet<>(Set.of(director1)))
                        .actors(new HashSet<>(Set.of(actor1, actor2)))
                        .build()
        ));
    }

}
