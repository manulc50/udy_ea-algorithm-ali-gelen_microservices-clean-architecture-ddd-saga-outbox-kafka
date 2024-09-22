package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {
    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public RestaurantApprovalResponseKafkaListener(
            RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener,
            OrderMessagingDataMapper orderMessagingDataMapper) {
        this.restaurantApprovalResponseMessageListener = restaurantApprovalResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-response-topic-name}")
    @Override
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of restaurant approval responses received with keys {}, partitions {} and offsets {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());
        messages.forEach(restaurantApprovalResponseAvroModel -> {
            if(OrderApprovalStatus.APPROVED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                log.info("Processing approved order for id: {}", restaurantApprovalResponseAvroModel.getOrderId());
                restaurantApprovalResponseMessageListener.orderApproved(orderMessagingDataMapper
                        .restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(restaurantApprovalResponseAvroModel));
            }
            else if(OrderApprovalStatus.REJECTED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                log.info("Processing rejected order for id: {}, with failure messages: {}",
                        restaurantApprovalResponseAvroModel.getOrderId(),
                        String.join(Order.FAILURE_MESSAGE_DELIMITER,
                                restaurantApprovalResponseAvroModel.getFailureMessages()));
                restaurantApprovalResponseMessageListener.orderRejected(orderMessagingDataMapper
                        .restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(restaurantApprovalResponseAvroModel));
            }
        });
    }
}
