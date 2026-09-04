package com.example.restaurant.controller;

import com.example.restaurant.dto.MenuItemDto;
import com.example.restaurant.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final RestaurantService restaurantService;

    public MenuController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<MenuItemDto> getMenu(@RequestParam UUID restaurantId) {
        return restaurantService.getMenuItems(restaurantId);
    }
}