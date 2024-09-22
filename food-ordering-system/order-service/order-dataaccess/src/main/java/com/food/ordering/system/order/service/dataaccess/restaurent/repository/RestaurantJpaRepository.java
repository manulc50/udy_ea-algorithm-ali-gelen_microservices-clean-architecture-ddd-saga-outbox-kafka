package com.food.ordering.system.order.service.dataaccess.restaurent.repository;

import com.food.ordering.system.order.service.dataaccess.restaurent.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurent.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {
    Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);
}
