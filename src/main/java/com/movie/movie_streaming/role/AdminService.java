package com.movie.movie_streaming.role;


import com.movie.movie_streaming.exceptions.UserNotFoundException;
import com.movie.movie_streaming.role.dto.UpdateRoleRequest;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    public void updateUserAuthorities(Integer userId , UpdateRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Role newRole = Role.valueOf(request.role().toUpperCase());

        if (user.getRole().equals(newRole)) {
            throw new IllegalArgumentException("User already has the role: " + newRole);
        }

        user.setRole(newRole);
        userRepository.save(user);
    }
}
