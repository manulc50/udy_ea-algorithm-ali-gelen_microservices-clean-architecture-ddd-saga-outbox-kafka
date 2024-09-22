package com.food.ordering.system.order.service.dataaccess.restaurent.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantEntityId {
    private UUID restaurantId;
    private UUID productId;
}
