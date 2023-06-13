package com.cydeo.service;

import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.UpdateOrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> retrieveOrderList();

    OrderDTO updateOrder(OrderDTO orderDTO);

    OrderDTO updateOrderById(Long id, UpdateOrderDTO updateOrderDTO);
}
