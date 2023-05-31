package com.cydeo.loosely;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class BalanceManager {
    public boolean checkout(BigDecimal checkoutAmount, Balance balance){

        return balance.getAmount().subtract(checkoutAmount)
                .compareTo(BigDecimal.ZERO) > 0;
    }
}
