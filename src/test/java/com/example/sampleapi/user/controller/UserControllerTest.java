package com.example.sampleapi.user.controller;

import com.example.sampleapi.user.dto.UserCreateRequest;
import com.example.sampleapi.user.dto.UserResponse;
import com.example.sampleapi.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("GET /api/v1/users returns user list")
    void getAllUsers_success() throws Exception {
        UserResponse user = new UserResponse(1L, "Alice", "alice@example.com", LocalDateTime.now());
        given(userService.getAllUsers()).willReturn(List.of(user));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    @DisplayName("POST /api/v1/users with valid body creates user")
    void createUser_success() throws Exception {
        UserResponse response = new UserResponse(1L, "Bob", "bob@example.com", LocalDateTime.now());
        given(userService.createUser(any(UserCreateRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Bob",
                                    "email": "bob@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    @DisplayName("POST /api/v1/users with invalid email returns 400 Bad Request")
    void createUser_invalidEmail_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Bob",
                                    "email": "not-an-email"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
