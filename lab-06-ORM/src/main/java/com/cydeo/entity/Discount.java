package com.cydeo.entity;

import com.cydeo.enums.DiscountType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Discount extends BaseEntity {

    private String name;
    private BigDecimal discount;
    @Enumerated(EnumType.STRING)
    private Enum<DiscountType> discountType;

}
