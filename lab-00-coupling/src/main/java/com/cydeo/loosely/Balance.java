package com.cydeo.loosely;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Component
public abstract class Balance {

    private UUID userId;
    private BigDecimal amount;

    public abstract BigDecimal addBalance(BigDecimal amount);
}
