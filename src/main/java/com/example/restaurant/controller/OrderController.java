package com.example.restaurant.controller;

import com.example.restaurant.dto.OrderCreateDto;
import com.example.restaurant.dto.OrderDto;
import com.example.restaurant.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@Valid @RequestBody OrderCreateDto dto) {
        return orderService.createOrder(dto);
    }
}