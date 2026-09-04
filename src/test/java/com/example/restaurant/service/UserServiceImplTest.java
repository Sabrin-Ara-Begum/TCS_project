package com.example.restaurant.service;

import com.example.restaurant.dto.UserCreateDto;
import com.example.restaurant.dto.UserDto;
import com.example.restaurant.entity.User;
import com.example.restaurant.exception.BusinessValidationException;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreateDto dto;

    @BeforeEach
    void setUp() {
        dto = new UserCreateDto();
        dto.setEmail("test@test.com");
        dto.setPassword("password");
        dto.setFirstName("John");
        dto.setLastName("Doe");
    }

    @Test
    void createUser_Success() {
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setEmail(dto.getEmail());
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserDto result = userService.createUser(dto);
        assertNotNull(result);
        assertEquals(dto.getEmail(), result.getEmail());
    }

    @Test
    void createUser_EmailTaken() {
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));
        assertThrows(BusinessValidationException.class, () -> userService.createUser(dto));
    }
}