package com.cydeo.lab04springmvc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private Integer quantity;
    private Product product;
}
