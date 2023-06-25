package com.cydeo.service.integration;

import com.cydeo.entity.Cart;
import com.cydeo.entity.Customer;
import com.cydeo.entity.Order;
import com.cydeo.enums.PaymentMethod;
import com.cydeo.repository.CartRepository;
import com.cydeo.repository.CustomerRepository;
import com.cydeo.repository.OrderRepository;
import com.cydeo.service.CartService;
import com.cydeo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class OrderServiceImplIntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void should_place_order_when_payment_method_is_transfer() {

        Cart cart = cartRepository.findById(3L).get();
        Order orderBeforePlaceOrderMethod = orderRepository.findAllByCart(cart);

        assertNull(orderBeforePlaceOrderMethod);    // no order before placing

        BigDecimal paidPrice = orderService.placeOrder(PaymentMethod.TRANSFER, 3L, 56L);
        System.out.println(paidPrice);

        Order orderAfterPlaceOrderMethod = orderRepository.findAllByCart(cart);

        assertNotNull(orderAfterPlaceOrderMethod);  // order created
        assertThat(paidPrice).isEqualTo(new BigDecimal("901.00"));
    }

    @Test
    public void should_place_order_when_payment_method_is_credit_card() {

        Cart cart = cartRepository.findById(3L).get();
        Order orderBeforePlaceOrderMethod = orderRepository.findAllByCart(cart);

        assertNull(orderBeforePlaceOrderMethod);    // no order before placing

        BigDecimal paidPrice = orderService.placeOrder(PaymentMethod.CREDIT_CARD, 3L, 56L);
        System.out.println(paidPrice);

        Order orderAfterPlaceOrderMethod = orderRepository.findAllByCart(cart);

        assertNotNull(orderAfterPlaceOrderMethod);  // order created
        assertThat(paidPrice).isEqualTo(new BigDecimal("891.00"));
    }

    @Test
    public void should_place_order_with_discount_when_payment_method_is_credit_card() {

        Cart cart = cartRepository.findById(3L).get();
        Order orderBeforePlaceOrderMethod = orderRepository.findAllByCart(cart);

        assertNull(orderBeforePlaceOrderMethod);    // no order before placing
        assertNull(cart.getDiscount());             // no discount applied yet

        BigDecimal discountAmount = cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(
                "50 dollar", cart);

        assertThat(discountAmount).isEqualTo(new BigDecimal("50.00"));
        assertNotNull(cart.getDiscount());          // discount ^^ $50 applied

        BigDecimal paidPrice = orderService.placeOrder(PaymentMethod.CREDIT_CARD, 3L, 56L);
        System.out.println(paidPrice);

        Order orderAfterPlaceOrderMethod = orderRepository.findAllByCart(cart);

        assertNotNull(orderAfterPlaceOrderMethod);  // order created
        assertThat(paidPrice).isEqualTo(new BigDecimal("841.00"));
    }

    @Test
    public void should_not_place_order_when_customer_NOT_exist() {

        Throwable throwable = catchThrowable(() -> orderService.placeOrder(
                PaymentMethod.CREDIT_CARD, 1L, 0L));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(throwable).hasMessage("Customer couldn't find");
    }

    @Test
    public void should_not_place_order_when_cart_NOT_exist() {

        Customer customer = new Customer();
        customerRepository.save(customer);

        Throwable throwable = catchThrowable(() -> orderService.placeOrder(
                PaymentMethod.CREDIT_CARD, 0L, customer.getId()));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(throwable).hasMessage("Cart couldn't find or cart is empty");
    }

}
