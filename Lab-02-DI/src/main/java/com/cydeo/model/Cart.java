package com.cydeo.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
public class Cart {
    private Map<Product, Integer> productMap;
    private BigDecimal cartTotalAmount;
}
