package com.cydeo.service.impl;

import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.UpdateOrderDTO;
import com.cydeo.entity.Order;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.OrderRepository;
import com.cydeo.service.CartService;
import com.cydeo.service.CustomerService;
import com.cydeo.service.OrderService;
import com.cydeo.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MapperUtil mapperUtil;
    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, MapperUtil mapperUtil, CustomerService customerService, PaymentService paymentService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.mapperUtil = mapperUtil;
        this.customerService = customerService;
        this.paymentService = paymentService;
        this.cartService = cartService;
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
                () -> new RuntimeException("Order not found"));

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
                () -> new RuntimeException("Order not found"));

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
            throw new RuntimeException("No changes detected");
        }
    }

    private void validateRelatedFieldsExist(OrderDTO orderDTO) {
        // need 3 different services to make sure the fields exist

        if (!customerService.existById(orderDTO.getCustomerId())) {

            throw new RuntimeException("Customer not found");
        }
        if (!paymentService.existById(orderDTO.getPaymentId())) {

            throw new RuntimeException("Payment not found");
        }
        if (!cartService.existById(orderDTO.getCartId())) {

            throw new RuntimeException("Cart not found");
        }
    }

}
