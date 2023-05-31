package com.cydeo.model;

import lombok.*;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class Product {
    private String name;
    private int quantity;
    private int remainingQuantity;
    private BigDecimal price;
}
