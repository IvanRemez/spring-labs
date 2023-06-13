package com.cydeo.controller;

import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.UpdateOrderDTO;
import com.cydeo.model.ResponseWrapper;
import com.cydeo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> retrieveOrderList() {

        return ResponseEntity.ok(new ResponseWrapper("Orders are successfully retrieved",
                orderService.retrieveOrderList(), HttpStatus.OK));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateOrder(@RequestBody OrderDTO orderDTO) {

        return ResponseEntity.ok(new ResponseWrapper("Order updated successfully",
                orderService.updateOrder(orderDTO), HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper> updateOrderById(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateOrderDTO updateOrderDTO) {
        // @Valid before @RequestBody -> makes sure the Validation annotations inside
        // UpdateOrderDTO class are activated

        return ResponseEntity.ok(new ResponseWrapper("Order updated successfully",
                orderService.updateOrderById(id, updateOrderDTO), HttpStatus.OK));
    }

}