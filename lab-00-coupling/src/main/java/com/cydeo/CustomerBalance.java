package com.cydeo;

import com.cydeo.loosely.Balance;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class CustomerBalance extends Balance {

    public BigDecimal addBalance(BigDecimal amount) {
        super.setAmount(amount);
        return amount;
    }
}
