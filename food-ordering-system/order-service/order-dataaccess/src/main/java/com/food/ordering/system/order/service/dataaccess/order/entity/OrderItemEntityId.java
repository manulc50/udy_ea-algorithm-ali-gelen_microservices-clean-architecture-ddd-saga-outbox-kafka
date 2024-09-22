package com.food.ordering.system.order.service.dataaccess.order.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntityId {
    private Long id;
    private OrderEntity order;
}
