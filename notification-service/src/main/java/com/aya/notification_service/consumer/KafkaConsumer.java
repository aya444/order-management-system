package com.aya.notification_service.consumer;

import com.aya.notification_service.feign.InventoryClient;
import com.aya.notification_service.feign.UserClient;
import com.aya.notification_service.service.NotificationService;
import com.aya.order_service.dto.OrderDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final UserClient userClient;
    private final InventoryClient inventoryClient;
    private final NotificationService notificationService;

    public String emailBuilder(OrderDto order) {
        StringBuilder emailBody = new StringBuilder();
        for (Integer productId : order.getProductsIds()) {
            String productName = inventoryClient.getProductName(productId).getBody();
            Double productPrice = inventoryClient.getProductPriceById(productId).getBody();
            Integer productQuantity = inventoryClient.getProductQuantityById(productId).getBody();

            if (productName == null || productPrice == null || productQuantity == null) {
                log.error("Missing product details for productId: {}", productId);
                continue; // Skip this product
            }

            emailBody.append(String.format(
                    "Product Name: %s%nProduct Price: %.2f%nProduct Quantity: %d%n%n",
                    productName, productPrice, productQuantity));
        }

        return String.format(
                "Dear Customer,%n%nYour order with total price %.2f has been completed. The order details are as follows:%n%n%s",
                order.getTotalPrice(), emailBody.toString());
    }


    public String emailReceiver(Integer customerId) {
        try {
            return userClient.getCustomerEmailById(customerId).getBody();
        } catch (FeignException e) {
            log.error("Failed to fetch email for customerId: {}", customerId, e);
            return null; // Return null or a default email
        }
    }


    @KafkaListener(topics = "completed-orders-topic", groupId = "notification-group")
    public void consume(OrderDto orderDto) {
        log.info("Consumed order from Kafka: {}", orderDto.toString());
        notificationService.sendEmail(emailReceiver(orderDto.getCustomerId()), emailBuilder(orderDto));
    }
}
