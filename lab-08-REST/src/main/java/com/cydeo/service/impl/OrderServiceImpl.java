package com.cydeo.service.impl;

import com.cydeo.client.CurrencyApiClient;
import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.UpdateOrderDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.CartState;
import com.cydeo.enums.Currency;
import com.cydeo.enums.PaymentMethod;
import com.cydeo.exception.CurrencyTypeNotFoundException;
import com.cydeo.exception.NotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.CartService;
import com.cydeo.service.CustomerService;
import com.cydeo.service.OrderService;
import com.cydeo.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${access_key}")
    private String accessKey;
    private final OrderRepository orderRepository;
    private final MapperUtil mapperUtil;
    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final CartService cartService;
    private final CurrencyApiClient currencyApiClient;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentRepository paymentRepository;

    public OrderServiceImpl(OrderRepository orderRepository, MapperUtil mapperUtil, CustomerService customerService,
                            PaymentService paymentService, CartService cartService, CurrencyApiClient currencyApiClient, CustomerRepository customerRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.mapperUtil = mapperUtil;
        this.customerService = customerService;
        this.paymentService = paymentService;
        this.cartService = cartService;
        this.currencyApiClient = currencyApiClient;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<OrderDTO> retrieveOrderList() {

        return orderRepository.findAll().stream()
                .map(order -> mapperUtil.convert(order, new OrderDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {

    // find Order by ID inside DB (or throw Exception)
        Order orderToUpdate = orderRepository.findById(orderDTO.getId()).orElseThrow(
                () -> new NotFoundException("Order not found"));

    // check if Order fields exist
        validateRelatedFieldsExist(orderDTO);

    // if fields exist, convert OrderDTO to Order and save it
        Order updatedOrder = orderRepository.save(
                mapperUtil.convert(orderDTO, new Order()));

        return mapperUtil.convert(updatedOrder, new OrderDTO());
    }

    @Override
    public OrderDTO updateOrderById(Long id, UpdateOrderDTO updateOrderDTO) {

        Order orderToUpdate = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Order not found"));

    // if User sends back same values, no need to update
        boolean changeDetected = false;

        if (!orderToUpdate.getPaidPrice().equals(updateOrderDTO.getPaidPrice())) {

            orderToUpdate.setPaidPrice(updateOrderDTO.getPaidPrice());
            changeDetected = true;
        }
        if (!orderToUpdate.getTotalPrice().equals(updateOrderDTO.getTotalPrice())) {

            orderToUpdate.setTotalPrice(updateOrderDTO.getTotalPrice());
            changeDetected = true;
        }
    // if there are changes, update and return the Order
        if (changeDetected) {
            Order updatedOrder = orderRepository.save(orderToUpdate);
            return mapperUtil.convert(updatedOrder, new OrderDTO());
        } else {
            throw new NotFoundException("No changes detected");
        }
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {

        Order order = orderRepository.save(mapperUtil.convert(orderDTO, new Order()));

        return mapperUtil.convert(order, new OrderDTO());
    }

    @Override
    public List<OrderDTO> getOrderListByPaymentMethod(PaymentMethod paymentMethod) {

        List<Order> orders = orderRepository.findAllByPayment_PaymentMethod(paymentMethod);

        return orders.stream().map(order -> mapperUtil.convert(
                order, new OrderDTO())).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrderListByEmail(String email) {

        List<Order> orders = orderRepository.findAllByCustomer_Email(email);

        return orders.stream().map(order -> mapperUtil.convert(
                order, new OrderDTO())).collect(Collectors.toList());
    }

    @Override
    public OrderDTO retrieveOrderDetailById(Long id, Optional<String> currency) {

        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Order not found"));

    // if we get Optional currency value from User:
        currency.ifPresent(curr -> {

            validateCurrency(curr);
        // ^^ validate proper currency input prior to sending to API to save money and time

            // get currency data based on currency type
            BigDecimal currencyRate = getCurrencyRate(curr);

            // do calculations and set new paidPrice and totalPrice
            BigDecimal newPaidPrice = order.getPaidPrice().multiply(currencyRate)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal newTotalPrice = order.getTotalPrice().multiply(currencyRate)
                    .setScale(2, RoundingMode.HALF_UP);
            // ^^ just for Customer Order - NOT saved in DB

            order.setPaidPrice(newPaidPrice);
            order.setTotalPrice(newTotalPrice);
        });

        return mapperUtil.convert(order, new OrderDTO());
    }

    private void validateCurrency(String curr) {

        // check if currency is Valid
        List<String> currencies = Stream.of(Currency.values())
                .map(currency -> currency.value)
                .collect(Collectors.toList());

        boolean validCurrency = currencies.contains(curr);

        if (!validCurrency) {
            throw new CurrencyTypeNotFoundException(
                    "Currency type for " + curr + " could not be found");
        }
    }

    private BigDecimal getCurrencyRate(String currency) {
    // consume the API
    // we have response inside the quotes Map
        Map<String, Double> quotes = (Map<String, Double>) currencyApiClient.getCurrencyRates(accessKey, currency,
                "USD", 1).get("quotes");
        // accessing quotes values using key ^^

    // check if API access is successful, THEN retrieve
        Boolean isSuccess = (Boolean) currencyApiClient.getCurrencyRates(accessKey, currency,
                // casting due ^^ to our generic Map return type inside our CurrencyApiClient
                "USD", 1).get("success");
        // accessing quotes values using key ^^

        if (!isSuccess) {
            throw new RuntimeException("API is down");
        }

        String expectedCurrency = "USD" + currency.toUpperCase();
        // ^^ need to format the Key (String) same as in JSON Response

        BigDecimal currencyRate = BigDecimal.valueOf(quotes.get(expectedCurrency));
        // ^^ use formatted Key to retrieve Value (currencyRate)
            // Cast to BigDecimal

        return currencyRate;
    }

    private void validateRelatedFieldsExist(OrderDTO orderDTO) {
        // need 3 different services to make sure the fields exist

        if (!customerService.existById(orderDTO.getCustomerId())) {

            throw new NotFoundException("Customer not found");
        }
        if (!paymentService.existById(orderDTO.getPaymentId())) {

            throw new NotFoundException("Payment not found");
        }
        if (!cartService.existById(orderDTO.getCartId())) {

            throw new NotFoundException("Cart not found");
        }
    }

    // Now, it's time to pay, show us you really want it, Also you get your dream discount. Well deserved.
    // Calm down... You will have it. There is only one click left...
    // You have selected desired product. We will have customer's money and place the order.
    // This method responsibility is to place the shiny order.
    // After that you can have a fresh air and refresh the page to see your order is shipped.
    // But first we have to accept the order. This method does that.
    @Override
    public BigDecimal placeOrder(PaymentMethod paymentMethod, Long cartId, Long customerId) {
        // we retrieve customer from DB, if not exists we need to throw exception
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer couldn't find"));

        // if a customer would like to place an order, cart should be created before.
        List<Cart> cartList = cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED);
        // if there is no cart in DB, we need to throw exception
        // Once customer place order cart status will be SOLD and after that if customer would like to buy something
        // again a new cart will be created. That's why a customer can have multiple carts but only one cart with CREATE status
        // can be exist run time. All other carts should be SOLD status
        if (cartList == null || cartList.size() == 0) {
            throw new RuntimeException("Cart couldn't find or cart is empty");
        }

        // according to business requirement there always be 1 cart with created state
        // That's why we are retrieving first index of cart list
        Cart cart = cartList.get(0);

        // We retrieve cart items in the cart
        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cartList.get(0));

        // if there is an item quantity that exceeds product remains quantity, we have to remove it from cart item list;
        cartItemList.removeIf(cartItem ->
                cartItem.getQuantity() > cartItem.getProduct().getRemainingQuantity());
        // if there is no item in the cart we are returning ZERO because this method should be returned paid price.
        // No item means you can not pay anything
        if (cartItemList.size() == 0){
            return BigDecimal.ZERO;
        }

        // Discounts can be applied to cart, but it is not mandatory. At first discount amount will be ZERO
        // If a discount can be applicable to cart, we will have discounted amount depends on discount
        // Before placing order discount must have been applied to cart.
        BigDecimal lastDiscountAmount = BigDecimal.ZERO;
        if (cart.getDiscount() != null) {
            lastDiscountAmount = cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(cart.getDiscount().getName(), cart);
        }

        // we are calculating te cart total amount to have gross amount
        BigDecimal totalCartAmount = calculateTotalCartAmount(cartItemList);

        Payment payment = new Payment();
        Order order = new Order();

        order.setCart(cart);
        order.setCustomer(customer);
        order.setTotalPrice(totalCartAmount);

        // total price $600
        // after discount = $50 -> $550

        // we are calculating the cart total amount after discount
        order.setPaidPrice(totalCartAmount.subtract(lastDiscountAmount));


        // let's assume if you pay with credit card we deduct 10 $ during the campaign period (Reward!!!)
        // additional discount for specific payment method for credit card
        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            order.setPaidPrice(order.getPaidPrice().subtract(BigDecimal.TEN));
        }

        // initialising payment entity
        // in the recordings, initialising payment was not inserted database directly.
        // But it caused other problems so i decided to insert it into DB first
        // After that we are setting payment value to Order Entity.
        payment.setPaidPrice(order.getPaidPrice());
        payment.setPaymentMethod(paymentMethod);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        // after successful order we decrease product remaining quantity
        // this stream is subtracting cart item quantity from product remaining quantity
        cartItemList.forEach(cartItem -> {
            cartItem.getProduct().setRemainingQuantity(
                    cartItem.getProduct().getRemainingQuantity() - cartItem.getQuantity());
            cartItemRepository.save(cartItem);
        });

        // stock is 50
        // customer bought 18
        // new stock will be 32
        orderRepository.save(order);
        return order.getPaidPrice();
    }

    private BigDecimal calculateTotalCartAmount(List<CartItem> cartItemList) {
        // this stream basically calculates the cart total amount depends on how many product is added to cart and theirs quantity
        // there is also another same method that calculate cart total amount in CartService we should use it for readability
        // but I paste it here to be able to show you more test cases.
        Function<CartItem, BigDecimal> totalMapper = cartItem -> cartItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return cartItemList.stream()
                .map(totalMapper)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
