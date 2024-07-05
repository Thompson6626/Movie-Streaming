package com.movie.movie_streaming;


import com.movie.movie_streaming.comment.Comment;
import com.movie.movie_streaming.role.Role;
import com.movie.movie_streaming.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class TestUtils {

    public static User generateUser(){
        return User.builder()
                .username("Username1")
                .password(new BCryptPasswordEncoder().encode("password1"))
                .email("fakeemail@gmail.com")
                .accountLocked(false)
                .enabled(true)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .role(Role.USER)
                .build();
    }
    public static Comment generateComment(){
        return Comment.builder()
                .stars(5.0)
                .body("This is a great movie")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }
    public static Authentication setAuthentication(User user){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
