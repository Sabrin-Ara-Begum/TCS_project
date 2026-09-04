package com.example.restaurant.service;
import com.example.restaurant.dto.*;
import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantDto createRestaurant(RestaurantCreateDto dto);
    List<RestaurantDto> getActiveRestaurants();
    RestaurantDto getRestaurant(UUID id);
    MenuItemDto addMenuItem(UUID restaurantId, MenuItemCreateDto dto);
    List<MenuItemDto> getMenuItems(UUID restaurantId);
}