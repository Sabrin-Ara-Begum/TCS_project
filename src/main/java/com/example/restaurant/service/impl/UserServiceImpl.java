package com.example.restaurant.service.impl;

import com.example.restaurant.dto.UserCreateDto;
import com.example.restaurant.dto.UserDto;
import com.example.restaurant.entity.User;
import com.example.restaurant.exception.BusinessValidationException;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessValidationException("Email already in use");
        }
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setPasswordHash(dto.getPassword()); // In real app, hash this!
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setPhone(dto.getPhone());
        User saved = userRepository.save(u);
        
        UserDto out = new UserDto();
        out.setId(saved.getId());
        out.setEmail(saved.getEmail());
        out.setFirstName(saved.getFirstName());
        out.setLastName(saved.getLastName());
        out.setPhone(saved.getPhone());
        out.setStatus(saved.getStatus());
        return out;
    }
}