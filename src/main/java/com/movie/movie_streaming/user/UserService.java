package com.movie.movie_streaming.user;

import com.movie.movie_streaming.Utilities.Utils;
import com.movie.movie_streaming.common.PageResponse;
import com.movie.movie_streaming.exceptions.*;
import com.movie.movie_streaming.movie.Movie;
import com.movie.movie_streaming.movie.MovieMapper;
import com.movie.movie_streaming.movie.MovieRepository;
import com.movie.movie_streaming.movie.dto.MovieResponse;
import com.movie.movie_streaming.user.dto.ChangePasswordRequest;
import com.movie.movie_streaming.user.dto.UpdateEmailRequest;
import com.movie.movie_streaming.user.dto.UpdateUsernameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Transactional
    public void changePassword(ChangePasswordRequest request, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new NewPasswordDoesNotMatchException();
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public PageResponse<MovieResponse> findAllFavourites(int page, int size, Authentication authentication) {
        Pageable pageable = PageRequest.of(page,size);
        int userId = ((User) authentication.getPrincipal()).getId();
        Page<Movie> movies = userRepository.findFavouritesByUser(userId,pageable);

        return Utils.generatePageResponse(movies,movieMapper::toResponse);
    }

    @Transactional
    public void addToFavourites(Integer movieId,Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        int id = user.getId();

        user = userRepository.findByIdWithFavourites(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));

        if(user.getFavourites().contains(movie)){
            throw new RuntimeException("Movie is already in favourites.");
        }

        user.getFavourites().add(movie);
        userRepository.save(user);
    }
    @Transactional
    public void removeFromFavourites(Integer movieId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        int id = user.getId();

        user = userRepository.findByIdWithFavourites(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));

        if(!user.getFavourites().contains(movie)){
            throw new RuntimeException("Movie was not present in favourites.");
        }
        user.getFavourites().remove(movie);

        userRepository.save(user);
    }

    @Transactional
    public void changeUsername(UpdateUsernameRequest request, Authentication connectedUser){
        User user = (User) connectedUser.getPrincipal();
        if (userRepository.findByUsername(request.newUsername()).isPresent()){
            throw new UsernameAlreadyTakenException();
        }
        user.setUsername(request.newUsername());
        userRepository.save(user);
    }

    @Transactional
    public void changeEmail(UpdateEmailRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        if (userRepository.findByEmail(request.newEmail()).isPresent()){
            throw new EmailAlreadyTakenException();
        }
        user.setEmail(request.newEmail());
        userRepository.save(user);
    }


}
