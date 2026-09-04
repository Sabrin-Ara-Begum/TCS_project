package com.example.restaurant.service.impl;

import com.example.restaurant.dto.*;
import com.example.restaurant.entity.MenuItem;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.service.RestaurantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @Transactional
    public RestaurantDto createRestaurant(RestaurantCreateDto dto) {
        Restaurant r = new Restaurant();
        r.setName(dto.getName());
        r.setDescription(dto.getDescription());
        r.setCuisine(dto.getCuisine());
        r.setDeliveryFee(dto.getDeliveryFee());
        r.setMinOrderAmount(dto.getMinOrderAmount());
        Restaurant saved = restaurantRepository.save(r);
        return mapToDto(saved);
    }

    @Override
    public List<RestaurantDto> getActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantDto getRestaurant(UUID id) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        return mapToDto(r);
    }

    @Override
    @Transactional
    public MenuItemDto addMenuItem(UUID restaurantId, MenuItemCreateDto dto) {
        Restaurant r = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        
        MenuItem item = new MenuItem();
        item.setRestaurant(r);
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setCalories(dto.getCalories());
        
        MenuItem saved = menuItemRepository.save(item);
        return mapToDto(saved);
    }

    @Override
    public List<MenuItemDto> getMenuItems(UUID restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found");
        }
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RestaurantDto mapToDto(Restaurant r) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setDescription(r.getDescription());
        dto.setCuisine(r.getCuisine());
        dto.setDeliveryFee(r.getDeliveryFee());
        dto.setMinOrderAmount(r.getMinOrderAmount());
        dto.setIsActive(r.getIsActive());
        return dto;
    }

    private MenuItemDto mapToDto(MenuItem m) {
        MenuItemDto dto = new MenuItemDto();
        dto.setId(m.getId());
        dto.setRestaurantId(m.getRestaurant().getId());
        dto.setName(m.getName());
        dto.setDescription(m.getDescription());
        dto.setPrice(m.getPrice());
        dto.setCalories(m.getCalories());
        dto.setIsAvailable(m.getIsAvailable());
        return dto;
    }
}