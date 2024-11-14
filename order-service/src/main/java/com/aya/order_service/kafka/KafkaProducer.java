package com.aya.order_service.kafka;

import com.aya.order_service.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // for logging
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private final NewTopic topic;
    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    public void sendMessage(OrderDto orderDto) {
        LOGGER.info("Order => {}", orderDto);
        // create message
        Message<OrderDto> message = MessageBuilder
                .withPayload(orderDto)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(message);
    }
}

