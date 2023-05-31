package com.cydeo.config;

import com.cydeo.Currency;
import com.cydeo.account.Current;
import com.cydeo.account.Saving;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.UUID;

@Configuration
public class Config {

    @Bean
    public Currency currency() {
        return new Currency("$", "USD");
    }
    @Bean
    public Current current() {
        return new Current(currency(), new BigDecimal(100), UUID.randomUUID());
    }

    @Bean
    public Saving saving() {
        return new Saving(currency(), new BigDecimal(150), UUID.randomUUID());
    }


}
