package com.cydeo;

import com.cydeo.loosely.Balance;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.UUID;

@Data
@Component
public class GiftCardBalance extends Balance {

    public BigDecimal addBalance(BigDecimal amount) {
        BigDecimal bonusAmount =
                amount.multiply(BigDecimal.TEN)
                        .divide(new BigDecimal(100)
                                , MathContext.DECIMAL64);

        super.setAmount(amount.add(bonusAmount));
        return amount;
    }
}
