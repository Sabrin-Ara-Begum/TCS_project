package com.example.restaurant.controller;

import com.example.restaurant.dto.UserCreateDto;
import com.example.restaurant.dto.UserDto;
import com.example.restaurant.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_Success() throws Exception {
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail("test@test.com");
        dto.setPassword("123456");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        UserDto resp = new UserDto();
        resp.setId(UUID.randomUUID());
        resp.setEmail("test@test.com");

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(resp);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }
    
    @Test
    void createUser_ValidationError() throws Exception {
        UserCreateDto dto = new UserCreateDto();
        // Missing fields to trigger @Valid
        
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}