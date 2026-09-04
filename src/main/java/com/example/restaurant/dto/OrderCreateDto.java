package com.example.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class OrderCreateDto {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    private String deliveryNotes;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemCreateDto> items;

    // Getters and Setters
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getDeliveryNotes() { return deliveryNotes; }
    public void setDeliveryNotes(String deliveryNotes) { this.deliveryNotes = deliveryNotes; }
    public List<OrderItemCreateDto> getItems() { return items; }
    public void setItems(List<OrderItemCreateDto> items) { this.items = items; }
}
