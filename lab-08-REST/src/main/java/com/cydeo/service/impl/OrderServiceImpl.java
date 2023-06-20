package com.cydeo.service.impl;

import com.cydeo.client.CurrencyApiClient;
import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.UpdateOrderDTO;
import com.cydeo.entity.Order;
import com.cydeo.enums.Currency;
import com.cydeo.enums.PaymentMethod;
import com.cydeo.exception.CurrencyTypeNotFoundException;
import com.cydeo.exception.NotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.OrderRepository;
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

    public OrderServiceImpl(OrderRepository orderRepository, MapperUtil mapperUtil, CustomerService customerService,
                            PaymentService paymentService, CartService cartService, CurrencyApiClient currencyApiClient) {
        this.orderRepository = orderRepository;
        this.mapperUtil = mapperUtil;
        this.customerService = customerService;
        this.paymentService = paymentService;
        this.cartService = cartService;
        this.currencyApiClient = currencyApiClient;
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

}
