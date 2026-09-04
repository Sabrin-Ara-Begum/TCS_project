package com.example.restaurant.service.impl;

import com.example.restaurant.dto.OrderCreateDto;
import com.example.restaurant.dto.OrderDto;
import com.example.restaurant.dto.OrderItemCreateDto;
import com.example.restaurant.dto.OrderItemDto;
import com.example.restaurant.entity.MenuItem;
import com.example.restaurant.entity.Order;
import com.example.restaurant.entity.OrderItem;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.entity.User;
import com.example.restaurant.exception.BusinessValidationException;
import com.example.restaurant.exception.ResourceNotFoundException;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                            RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderCreateDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!restaurant.getIsActive()) {
            throw new BusinessValidationException("Restaurant is not currently active");
        }

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setDeliveryNotes(dto.getDeliveryNotes());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemCreateDto itemDto : dto.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + itemDto.getMenuItemId()));

            if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                throw new BusinessValidationException("Menu item " + menuItem.getName() + " does not belong to the restaurant");
            }
            if (!menuItem.getIsAvailable()) {
                throw new BusinessValidationException("Menu item " + menuItem.getName() + " is currently unavailable");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(menuItem.getPrice());
            
            BigDecimal itemTotal = menuItem.getPrice().multiply(new BigDecimal(itemDto.getQuantity()));
            orderItem.setTotalPrice(itemTotal);
            orderItem.setSpecialInstructions(itemDto.getSpecialInstructions());
            
            orderItems.add(orderItem);
            subtotal = subtotal.add(itemTotal);
        }

        if (restaurant.getMinOrderAmount() != null && subtotal.compareTo(restaurant.getMinOrderAmount()) < 0) {
            throw new BusinessValidationException("Order subtotal must be at least " + restaurant.getMinOrderAmount());
        }

        order.setOrderItems(orderItems);
        order.setSubtotal(subtotal);
        order.setDeliveryFee(restaurant.getDeliveryFee() != null ? restaurant.getDeliveryFee() : BigDecimal.ZERO);
        order.setTotal(order.getSubtotal().add(order.getDeliveryFee()));

        Order saved = orderRepository.save(order);
        return mapToDto(saved);
    }

    private OrderDto mapToDto(Order o) {
        OrderDto dto = new OrderDto();
        dto.setId(o.getId());
        dto.setUserId(o.getUser().getId());
        dto.setRestaurantId(o.getRestaurant().getId());
        dto.setSubtotal(o.getSubtotal());
        dto.setDeliveryFee(o.getDeliveryFee());
        dto.setTotal(o.getTotal());
        dto.setDeliveryAddress(o.getDeliveryAddress());
        dto.setDeliveryNotes(o.getDeliveryNotes());
        dto.setStatus(o.getStatus());
        
        if (o.getOrderItems() != null) {
            dto.setItems(o.getOrderItems().stream().map(this::mapToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    private OrderItemDto mapToDto(OrderItem oi) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(oi.getId());
        dto.setMenuItemId(oi.getMenuItem().getId());
        dto.setMenuItemName(oi.getMenuItem().getName());
        dto.setQuantity(oi.getQuantity());
        dto.setUnitPrice(oi.getUnitPrice());
        dto.setTotalPrice(oi.getTotalPrice());
        dto.setSpecialInstructions(oi.getSpecialInstructions());
        return dto;
    }
}