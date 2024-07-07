package com.movie.movie_streaming.services;

import com.movie.movie_streaming.exceptions.UserNotFoundException;
import com.movie.movie_streaming.role.AdminService;
import com.movie.movie_streaming.role.Role;
import com.movie.movie_streaming.role.UpdateRoleRequest;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;


    @Test
    public void updateUserAuthorities_userNotFound() {

        var userId = 1;
        var request = new UpdateRoleRequest("ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            adminService.updateUserAuthorities(userId, request);
        });

        verify(userRepository).findById(userId);
    }

    @Test
    public void updateUserAuthorities_roleAlreadyAssigned() {
        var userId = 1;
        var request = new UpdateRoleRequest("ADMIN");

        var user = User.builder()
                .id(userId)
                .role(Role.ADMIN)
                .build();


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateUserAuthorities(userId, request);
        });

        assertEquals("User already has the role: ADMIN", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    public void updateUserAuthorities_successfulUpdate() {
        var userId = 1;
        var request = new UpdateRoleRequest("MANAGER");

        var user = User.builder()
                .id(userId)
                .role(Role.USER)
                .build();

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));

        adminService.updateUserAuthorities(userId, request);

        assertEquals(Role.MANAGER, user.getRole());
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }
}

