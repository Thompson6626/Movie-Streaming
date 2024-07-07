package com.movie.movie_streaming.services;

import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.exceptions.EmailAlreadyTakenException;
import com.movie.movie_streaming.exceptions.IncorrectPasswordException;
import com.movie.movie_streaming.exceptions.NewPasswordDoesNotMatchException;
import com.movie.movie_streaming.exceptions.UsernameAlreadyTakenException;
import com.movie.movie_streaming.movie.Movie;
import com.movie.movie_streaming.movie.MovieMapper;
import com.movie.movie_streaming.movie.MovieRepository;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import com.movie.movie_streaming.user.UserService;
import com.movie.movie_streaming.user.dto.ChangePasswordRequest;
import com.movie.movie_streaming.user.dto.UpdateEmailRequest;
import com.movie.movie_streaming.user.dto.UpdateUsernameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1)
                .username("testUser")
                .password("encodedPassword")
                .build();

        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    public void testChangePassword_Success() {
        var request = ChangePasswordRequest.builder()
                .currentPassword("currentPassword")
                .newPassword("newPassword")
                .confirmationPassword("newPassword")
                .build();

        when(passwordEncoder.matches(eq("currentPassword"), eq("encodedPassword"))).thenReturn(true);
        when(passwordEncoder.encode(eq("newPassword"))).thenReturn("newEncodedPassword");

        userService.changePassword(request, authentication);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testChangePassword_IncorrectCurrentPassword() {
        var request = ChangePasswordRequest.builder()
                .currentPassword("wrongCurrentPassword")
                .newPassword("newPassword")
                .confirmationPassword("newPassword")
                .build();

        when(passwordEncoder.matches(eq("wrongCurrentPassword"), eq("encodedPassword"))).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> {
            userService.changePassword(request, authentication);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testChangePassword_NewPasswordMismatch() {
        ChangePasswordRequest request = new ChangePasswordRequest("currentPassword", "newPassword", "differentPassword");

        when(passwordEncoder.matches(eq("currentPassword"), eq("encodedPassword"))).thenReturn(true);

        assertThrows(NewPasswordDoesNotMatchException.class, () -> {
            userService.changePassword(request, authentication);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testFindAllFavourites() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(new Movie()));

        when(userRepository.findFavouritesByUser(eq(1), eq(pageable))).thenReturn(moviePage);

        PageResponse<MovieResponse> response = userService.findAllFavourites(page, size, authentication);

        assertNotNull(response);
        verify(userRepository).findFavouritesByUser(eq(1), eq(pageable));
    }

    @Test
    public void testAddToFavourites() {
        var movie = Movie.builder()
                .id(1)
                .build();

        Set<Movie> favourites = new HashSet<>();
        user.setFavourites(favourites);

        when(userRepository.findByIdWithFavourites(eq(1))).thenReturn(Optional.of(user));
        when(movieRepository.findById(eq(1))).thenReturn(Optional.of(movie));

        userService.addToFavourites(1, authentication);

        assertTrue(user.getFavourites().contains(movie));
        verify(userRepository).save(user);
    }

    @Test
    public void testRemoveFromFavourites() {
        var movie = Movie.builder()
                .id(1)
                .build();

        Set<Movie> favourites = new HashSet<>(Collections.singleton(movie));
        user.setFavourites(favourites);

        when(userRepository.findByIdWithFavourites(eq(1))).thenReturn(Optional.of(user));
        when(movieRepository.findById(eq(1))).thenReturn(Optional.of(movie));

        userService.removeFromFavourites(1, authentication);

        assertFalse(user.getFavourites().contains(movie));
        verify(userRepository).save(user);
    }

    @Test
    public void testChangeUsername_Success() {
        var request = new UpdateUsernameRequest("newUsername");

        when(userRepository.findByUsername(eq("newUsername"))).thenReturn(Optional.empty());

        userService.changeUsername(request, authentication);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testChangeUsername_AlreadyTaken() {
        var request = new UpdateUsernameRequest("newUsername");

        when(userRepository.findByUsername(eq("newUsername"))).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyTakenException.class, () -> {
            userService.changeUsername(request, authentication);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testChangeEmail_Success() {
        var request = new UpdateEmailRequest("newEmail@example.com");

        when(userRepository.findByEmail(eq("newEmail@example.com"))).thenReturn(Optional.empty());

        userService.changeEmail(request, authentication);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testChangeEmail_AlreadyTaken() {
        var request = new UpdateEmailRequest("newEmail@example.com");

        when(userRepository.findByEmail(eq("newEmail@example.com"))).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyTakenException.class, () -> {
            userService.changeEmail(request, authentication);
        });

        verify(userRepository, never()).save(any(User.class));
    }

}
