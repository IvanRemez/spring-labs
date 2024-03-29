package com.cydeo.repository;

import com.cydeo.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest    // to specify to Spring that you only want to test the REPO Classes
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void should_retrieve_top_5_by_price_DESC() {

        List<Order> orderList = orderRepository.findTop5ByOrderByTotalPriceDesc();

        assertThat(orderList).hasSize(5);
        assertThat(orderList.get(0).getTotalPrice()).isEqualTo(new BigDecimal("2994.60"));
    }

    @Test
    public void should_get_order_total_price_and_paid_price_is_SAME() {

        List<Order> orderList = orderRepository.retrieveAllOrdersBetweenTotalPriceAndPaidPriceIsSame();
        assertThat(orderList).hasSize(2);
    }

    @Test
    public void should_get_all_orders_by_category_id() {

        List<Order> orderList = orderRepository.retrieveAllOrdersByCategoryId(1L);
        assertThat(orderList).hasSize(2);
    }

}
