package com.example.restaurant.controller;

import com.example.restaurant.dto.*;
import com.example.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantDto> getRestaurants() {
        return restaurantService.getActiveRestaurants();
    }

    @GetMapping("/{id}")
    public RestaurantDto getRestaurant(@PathVariable UUID id) {
        return restaurantService.getRestaurant(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDto createRestaurant(@Valid @RequestBody RestaurantCreateDto dto) {
        return restaurantService.createRestaurant(dto);
    }

    @PostMapping("/{id}/menu-items")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemDto addMenuItem(@PathVariable UUID id, @Valid @RequestBody MenuItemCreateDto dto) {
        return restaurantService.addMenuItem(id, dto);
    }
}