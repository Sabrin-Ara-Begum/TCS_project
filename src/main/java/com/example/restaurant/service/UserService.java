package com.example.restaurant.service;
import com.example.restaurant.dto.UserCreateDto;
import com.example.restaurant.dto.UserDto;

public interface UserService {
    UserDto createUser(UserCreateDto dto);
}