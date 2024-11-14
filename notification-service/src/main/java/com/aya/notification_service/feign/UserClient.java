package com.aya.notification_service.feign;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USER-SERVICE")
public interface UserClient {

    @GetMapping("/admin/get-email-by-id/{id}")
    ResponseEntity<String> getCustomerEmailById(@PathVariable("id") @NotNull Integer id);
}

