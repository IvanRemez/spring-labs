package com.cydeo.service.integration;

import com.cydeo.entity.Cart;
import com.cydeo.entity.CartItem;
import com.cydeo.entity.Customer;
import com.cydeo.entity.Product;
import com.cydeo.enums.CartState;
import com.cydeo.repository.CartItemRepository;
import com.cydeo.repository.CartRepository;
import com.cydeo.repository.CustomerRepository;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CartServiceImplIntegrationTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void should_add_to_cart_WITHOUT_existing_cart() {

        Customer customer = new Customer();
        // ^^ only need to set values if validations set up
        customerRepository.save(customer);

        boolean productAdded = cartService.addToCart(customer, 1L, 10);

        List<Cart> cartList = cartRepository.findAllByCustomerIdAndCartState(
                customer.getId(), CartState.CREATED);

        Product product = productRepository.findById(1L).get();
        CartItem cartItem = cartItemRepository.findAllByCartAndProduct(
                cartList.get(0), product);

        assertTrue(productAdded);   // product successfully added
        assertThat(cartList).hasSize(1);    // cart successfully auto created
        assertNotNull(cartItem);  // product successfully transferred to Cart as cartItem
    }

    @Test
    public void should_add_to_cart_with_existing_cart() {

        Customer customer = customerRepository.findById(40L).get();

        boolean productAdded = cartService.addToCart(customer, 1L, 10);

        List<Cart> cartList = cartRepository.findAllByCustomerIdAndCartState(
                customer.getId(), CartState.CREATED);

        Product product = productRepository.findById(1L).get();
        CartItem cartItem = cartItemRepository.findAllByCartAndProduct(
                cartList.get(0), product);

        assertTrue(productAdded);   // product successfully added
        assertThat(cartList).hasSize(1);    // cart successfully auto created
        assertNotNull(cartItem);  // product successfully transferred to Cart as cartItem
    }

    @Test
    public void should_NOT_add_to_cart_when_insufficient_stock() {

        Customer customer = customerRepository.findById(40L).get();

        Throwable throwable = catchThrowable(() ->
                cartService.addToCart(customer, 1L, 450));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void should_apply_amount_based_discount_to_cart_existing() {

        Cart cart = cartRepository.findById(3L).get();
        // ^^ get cart with No discount (null) to test if applyDiscount..() method works

        BigDecimal result = cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(
                "50 dollar", cart);

        assertNotNull(cart.getDiscount());
        assertThat(result).isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    public void should_apply_rate_based_discount_to_cart_existing() {

        Cart cart = cartRepository.findById(3L).get();
        // ^^ get cart with No discount (null) to test if applyDiscount..() method works

        BigDecimal result = cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(
                "%25", cart);

        assertNotNull(cart.getDiscount());
        assertThat(result).isEqualTo(new BigDecimal("225.2500"));
    }

    @Test
    public void should_apply_amount_based_discount_to_cart_NOT_existing() {

        Customer customer = new Customer();
        customerRepository.save(customer);

        cartService.addToCart(customer, 1L, 10);

        List<Cart> cartList = cartRepository.retrieveCartListByCustomer(customer.getId());

        assertThat(cartList).hasSize(1);
        assertNull(cartList.get(0).getDiscount());

        cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(
                "50 dollar", cartList.get(0));
        assertNotNull(cartList.get(0).getDiscount());
    }

    @Test
    public void should_NOT_apply_amount_based_discount_to_cart_NOT_existing() {

        Customer customer = new Customer();
        customerRepository.save(customer);

        cartService.addToCart(customer, 1L, 1);
        // won't be enough for minimum amount towards Discount ^^

        List<Cart> cartList = cartRepository.retrieveCartListByCustomer(customer.getId());

        assertThat(cartList).hasSize(1);
        assertNull(cartList.get(0).getDiscount());

        cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(
                "50 dollar", cartList.get(0));
        assertNull(cartList.get(0).getDiscount());
        // ^^ minimum cart amount NOT enough so Discount will NOT be applied
    }

}