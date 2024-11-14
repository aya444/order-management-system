package com.aya.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInputDto {
    private Integer customerId;
    private List<ProductInputDto> productInputDtoIds;
}