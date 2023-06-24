package com.cydeo.service.unit;

import com.cydeo.entity.*;
import com.cydeo.enums.CartState;
import com.cydeo.enums.PaymentMethod;
import com.cydeo.repository.*;
import com.cydeo.service.CartService;
import com.cydeo.service.impl.OrderServiceImpl;
import org.checkerframework.checker.units.qual.C;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplUnitTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderServiceImpl orderService;


//  Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer couldn't find"));
    @Test
    public void should_throw_exception_when_customer_does_not_exist() {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() ->
                orderService.placeOrder(PaymentMethod.TRANSFER, 134L, 1L));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }

//if (cartList == null || cartList.size() == 0) {
//        throw new RuntimeException("Cart couldn't find or cart is empty");
//    }
    @Test
    public void should_throw_exception_when_customers_cart_list_is_null() {

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED))
                .thenReturn(null);

        Throwable throwable = catchThrowable(() ->
                orderService.placeOrder(PaymentMethod.TRANSFER, 134L, customer.getId()));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }

//if (cartList == null || cartList.size() == 0) {
//        throw new RuntimeException("Cart couldn't find or cart is empty");
//    }
    @Test
    public void should_throw_exception_when_customers_cart_list_size_zero() {

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED))
                .thenReturn(new ArrayList<>()); // <-- Empty cartList

        Throwable throwable = catchThrowable(() ->
                orderService.placeOrder(PaymentMethod.TRANSFER, 134L, customer.getId()));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void should_place_order_without_discount() {

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(5));
        product.setRemainingQuantity(60);

        Customer customer = new Customer();
        customer.setId(1L);

        Cart cart = new Cart();
        cart.setId(1L);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(4);
        cartItem.setProduct(product);
        cartItem.setCart(cart);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED))
                .thenReturn(cartList);
        when(cartItemRepository.findAllByCart(cartList.get(0))).thenReturn(cartItemList);

        BigDecimal result = orderService.placeOrder(PaymentMethod.TRANSFER, cart.getId(), customer.getId());

        assertThat(result).isEqualTo(BigDecimal.valueOf(20));
        // ^^ product -> cartItems(4) x price(5) = 20 ^^
        assertThat(product.getRemainingQuantity()).isEqualTo(56);
    }

    @Test
    public void should_place_order_with_discount() {

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(5));
        product.setRemainingQuantity(60);

        Customer customer = new Customer();
        customer.setId(1L);

        Discount discount = new Discount();
        discount.setName("discount");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setDiscount(discount);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(4);
        cartItem.setProduct(product);
        cartItem.setCart(cart);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED))
                .thenReturn(cartList);
        when(cartItemRepository.findAllByCart(cartList.get(0))).thenReturn(cartItemList);
        when(cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(
                discount.getName(), cart)).thenReturn(BigDecimal.TEN);

        BigDecimal result = orderService.placeOrder(PaymentMethod.TRANSFER, cart.getId(), customer.getId());

        assertThat(result).isEqualTo(BigDecimal.valueOf(10));
        // ^^ product -> cartItems(4) x price(5) = 20 - discount(10) = 10 ^^
        assertThat(product.getRemainingQuantity()).isEqualTo(56);
    }

    @Test
    public void should_place_order_with_discount_and_extra_credit_card_discount() {

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(5));
        product.setRemainingQuantity(60);

        Customer customer = new Customer();
        customer.setId(1L);

        Discount discount = new Discount();
        discount.setName("discount");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setDiscount(discount);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(10);
        cartItem.setProduct(product);
        cartItem.setCart(cart);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED))
                .thenReturn(cartList);
        when(cartItemRepository.findAllByCart(cartList.get(0))).thenReturn(cartItemList);
        when(cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(
                discount.getName(), cart)).thenReturn(BigDecimal.valueOf(15));

        BigDecimal result = orderService.placeOrder(PaymentMethod.CREDIT_CARD, cart.getId(), customer.getId());

        assertThat(result).isEqualTo(BigDecimal.valueOf(25));
        // ^^ product -> cartItems(4) x price(5) = 20 - discount(10) = 10 ^^
        assertThat(product.getRemainingQuantity()).isEqualTo(50);

        // Quantity = 10, Price = $5
        // Total cart amount = $50
        // Discount = $15 = new Total = $35
        // after CC payment discount ($10) = 35 - 10 = $25 Final Price
    }

// // if there is an item quantity that exceeds product remains quantity, we have to remove it from cart item list;
//        cartItemList.removeIf(cartItem ->
//                cartItem.getQuantity() > cartItem.getProduct().getRemainingQuantity());
    @Test
    public void should_not_place_order_when_item_is_removed_from_cart() {  // test ^^^

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(5));
        product.setRemainingQuantity(5);        // 5 items remaining

        Customer customer = new Customer();
        customer.setId(1L);

        Cart cart = new Cart();
        cart.setId(1L);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(10);               // 10 items in Cart
        cartItem.setProduct(product);
        cartItem.setCart(cart);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED))
                .thenReturn(cartList);
        when(cartItemRepository.findAllByCart(cartList.get(0))).thenReturn(cartItemList);

        BigDecimal result = orderService.placeOrder(PaymentMethod.CREDIT_CARD, cart.getId(), customer.getId());

        assertThat(result).isEqualTo(BigDecimal.ZERO);
        assertThat(product.getRemainingQuantity()).isEqualTo(5);
    }

// ***HOMEWORK***
// what about 2 item in the cart, one of them out of stock and the other can be processed

    @Test
    public void should_place_order_with_removing_items_when_one_of_them_out_of_stock_and_the_other_can_be_processed() {

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(5));
        product.setRemainingQuantity(10);        // 10 items remaining

        Product product2 = new Product();
        product2.setPrice(BigDecimal.valueOf(5));
        product2.setRemainingQuantity(10);      // 10 items remaining

        Customer customer = new Customer();
        customer.setId(1L);

        Cart cart = new Cart();
        cart.setId(1L);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);               // 5 items in Cart
        cartItem.setProduct(product);
        cartItem.setCart(cart);

        CartItem cartItem2 = new CartItem();
        cartItem2.setQuantity(15);               // 15 items in Cart
        cartItem2.setProduct(product2);
        cartItem2.setCart(cart);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        cartItemList.add(cartItem2);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED))
                .thenReturn(cartList);
        when(cartItemRepository.findAllByCart(cartList.get(0))).thenReturn(cartItemList);

        BigDecimal paidPrice = orderService.placeOrder(PaymentMethod.TRANSFER, cart.getId(), customer.getId());

        assertThat(paidPrice).isEqualTo(BigDecimal.valueOf(25));
        assertThat(product.getRemainingQuantity()).isEqualTo(5);
        assertThat(product2.getRemainingQuantity()).isEqualTo(10);
    }


}
