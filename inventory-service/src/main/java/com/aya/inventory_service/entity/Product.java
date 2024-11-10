package com.aya.inventory_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private Integer quantity;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
