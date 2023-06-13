package com.cydeo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateOrderDTO {

    // only UPDATABLE fields in DTO

    @NotNull(message = "price cannot be null")
    @Positive(message = "price shouldn't be negative")
    @DecimalMax(value = "100000",message = "price cannot be greater than 1000000")
    @DecimalMin(value = "0.1", message = "price cannot be less than 0.1")
    private BigDecimal paidPrice;

    @NotNull(message = "totalPrice cannot be null")
    @Positive(message = "totalPrice shouldn't be negative")
    @DecimalMax(value = "100000",message = "totalPrice cannot be greater than 1000000")
    @DecimalMin(value = "0.1", message = "totalPrice cannot be less than 0.1")
    private BigDecimal totalPrice;

}