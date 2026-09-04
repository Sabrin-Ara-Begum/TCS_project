package com.example.restaurant.service;
import com.example.restaurant.dto.OrderCreateDto;
import com.example.restaurant.dto.OrderDto;

public interface OrderService {
    OrderDto createOrder(OrderCreateDto dto);
}