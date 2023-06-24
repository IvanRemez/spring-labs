package com.cydeo.service.unit;

import com.cydeo.entity.Cart;
import com.cydeo.entity.CartItem;
import com.cydeo.entity.Customer;
import com.cydeo.entity.Product;
import com.cydeo.enums.CartState;
import com.cydeo.repository.CartItemRepository;
import com.cydeo.repository.CartRepository;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplUnitTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    public void should_not_add_to_cart_when_doesnt_exist() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
    // if above method ^^ returns null (Optional.empty()) -> Exception should be thrown
        Throwable throwable = catchThrowable(() ->
                cartService.addToCart(new Customer(), 1L, 10));
    // checking if the right Exception is thrown:
        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void should_throw_exception_when_product_remaining_quantity_is_less_than_quantity() {

        Product product = new Product();
        product.setRemainingQuantity(10);

    // need to provide this data prior to the if statement we are testing in this method:
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        // ^^ find Product by ID
        Throwable throwable = catchThrowable(() ->
                cartService.addToCart(new Customer(), 1L, 8));
        // ^^ addToCart() method needs 3 parameters ^^

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(throwable).hasMessage("Not enough stock");
        // ^^ this will fail the test if Exception is NOT thrown - no message
    }

    @Test
    public void should_add_item_to_cart_when_cart_exists_and_cart_item_exists_in_cart() {
// NEED:
    // product
        Product product = new Product();
        product.setRemainingQuantity(10);
        product.setId(1L);

        // cart and cartList
        Cart cart = new Cart();
        cart.setCartState(CartState.CREATED);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        // cart item
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(2);

        // customer
        Customer customer = new Customer();
        customer.setId(1L);

    // WHEN
        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));
        when(cartRepository.findAllByCustomerIdAndCartState(
                customer.getId(), CartState.CREATED))
                .thenReturn(cartList);
        when(cartItemRepository.findAllByCartAndProduct(cart, product))
                .thenReturn(cartItem);

    // THEN
        boolean result = cartService.addToCart(customer, product.getId(), 8);
        assertTrue(result);
        assertThat(cartItem.getQuantity()).isEqualTo(10);
    }

    @Test
    public void should_throw_an_exception_when_cart_list_size_is_two() {
// NEED:
        // product
        Product product = new Product();
        product.setRemainingQuantity(10);
        product.setId(1L);

        // cart and cartList
        Cart cart = new Cart();
        cart.setCartState(CartState.CREATED);

        Cart cart2 = new Cart();
        cart2.setCartState(CartState.CREATED);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);
        cartList.add(cart2);

        // customer
        Customer customer = new Customer();
        customer.setId(1L);
// WHEN:
        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));
        when(cartRepository.findAllByCustomerIdAndCartState(
                customer.getId(), CartState.CREATED))
                .thenReturn(cartList);
// THEN:

    }

    // homework
    // write unit test for scenarios
    // scenario 1 -> cart doesn't exist for customer or cart list is null (new Arraylist or null)
    // scenario 2 -> cart exist but cart item doesn't exist

    // scenario 1
//    @Test
//    public void should_add_item_to_cart_when_cart_doesnt_exist() {
//// NEED:
//        // product
//        Product product = new Product();
//        product.setId(1L);
//        product.setQuantity(10);
//        product.setName("Orange");
//        product.setPrice(BigDecimal.TEN);
//        product.setRemainingQuantity(10);
//
//        // customer
//        Customer customer = new Customer();
//        customer.setId(1L);
//// WHEN:
//        when(productRepository.findById(product.getId()))
//                .thenReturn(Optional.of(product));
//        when(cartRepository.findAllByCustomerIdAndCartState(
//                customer.getId(), CartState.CREATED))
//                .thenReturn(null);
//// THEN:
//        assertThat(cartService.createCartForCustomer(customer))
//    }

    // scenario 2
    @Test
    public void should_add_to_cart_when_cart_exists_and_cart_item_not_exist() {


    }

}
