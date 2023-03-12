package com.cydeo;
import com.cydeo.loosely.Balance;
import com.cydeo.loosely.BalanceManager;
import com.cydeo.tightly.BalanceService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        UUID user = UUID.randomUUID();

        Balance balance = new CustomerBalance();
        balance.setUserId(user);
        balance.setAmount(BigDecimal.ZERO);

        ApplicationContext container = SpringApplication.run(Main.class, args);

        BalanceManager balanceManager = container.getBean(BalanceManager.class);

        Balance customerBalance =
                container.getBean("customerBalance", CustomerBalance.class);
        Balance giftCardBalance =
                container.getBean("giftCardBalance", GiftCardBalance.class);

        customerBalance.addBalance(new BigDecimal(79));
        System.out.println(customerBalance.getAmount());

        giftCardBalance.addBalance(new BigDecimal(59));
        System.out.println(giftCardBalance.getAmount());

        System.out.println(balanceManager.checkout(new BigDecimal(80), customerBalance));
        System.out.println(balanceManager.checkout(new BigDecimal(60), giftCardBalance));

        System.exit(0);
    }

}
