package com.example.restaurant.controller;

import com.example.restaurant.dto.UserCreateDto;
import com.example.restaurant.dto.UserDto;
import com.example.restaurant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCreateDto dto) {
        return userService.createUser(dto);
    }
}