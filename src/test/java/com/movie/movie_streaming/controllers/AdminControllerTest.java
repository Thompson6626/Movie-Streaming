package com.movie.movie_streaming.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.movie_streaming.role.AdminService;
import com.movie.movie_streaming.role.UpdateRoleRequest;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(InstancioExtension.class)
@AutoConfigureMockMvc
public class AdminControllerTest {

    private final MockMvc mockMvc;
    @MockBean
    private AdminService adminService;
    private final ObjectMapper objectMapper;
    @Autowired
    public AdminControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updatingUsersRolesShouldReturn200() throws Exception {
        UpdateRoleRequest request = Instancio.create(UpdateRoleRequest.class);

        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(patch("/admin/users/1/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void nonAdminCannotAccessUpdateUserRole() throws Exception {
        mockMvc.perform(patch("/admin/users/1/role"))
                .andExpect(status().isForbidden());
    }


}

