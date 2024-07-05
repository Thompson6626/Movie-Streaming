package com.movie.movie_streaming.controllers;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.TestUtils;
import com.movie.movie_streaming.role.Role;
import com.movie.movie_streaming.role.UpdateRoleRequest;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, InstancioExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AdminControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @MockBean
    private AuditingHandler auditingHandler;
    @Autowired
    public AdminControllerIntegrationTests(
            UserRepository userRepository,
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void updatingUsersRolesShouldReturn200() throws Exception {
        User user = TestUtils.generateUser();

        userRepository.save(user);

        UpdateRoleRequest updateRoleRequest = new UpdateRoleRequest("Admin");

        String jsonReq = objectMapper.writeValueAsString(updateRoleRequest);

        mockMvc.perform(put("/admin/users/2/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonReq))
                .andExpect(status().isOk());

        Optional<User> foundUser = userRepository.findById(1);
        assertThat(foundUser)
                .isPresent()
                .get()
                .extracting(User::getRole)
                .isEqualTo(Role.ADMIN);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void accessingAdminWithoutAnyImportantRoleReturns403() throws Exception {
        mockMvc.perform(post("/admin/updateAuthorities"))
                .andExpect(status().isForbidden());
    }


}

