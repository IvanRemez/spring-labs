package com.cydeo.service;

import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.UpdateOrderDTO;
import com.cydeo.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDTO> retrieveOrderList();

    OrderDTO updateOrder(OrderDTO orderDTO);

    OrderDTO updateOrderById(Long id, UpdateOrderDTO updateOrderDTO);

    OrderDTO createOrder(OrderDTO orderDTO);

    List<OrderDTO> getOrderListByPaymentMethod(PaymentMethod paymentMethod);

    List<OrderDTO> getOrderListByEmail(String email);

    OrderDTO retrieveOrderDetailById(Long id, Optional<String> currency);

    BigDecimal placeOrder(PaymentMethod paymentMethod, Long cartId, Long customerId);
}
