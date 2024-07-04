package com.movie.movie_streaming.auth;


import com.movie.movie_streaming.user.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String FORMAT = "Bearer ";
        if (authHeader == null ||!authHeader.startsWith(FORMAT)) {
            return;
        }
        jwt = authHeader.substring(FORMAT.length());
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpiresAt(LocalDateTime.MIN);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}