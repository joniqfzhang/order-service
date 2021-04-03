package com.microservice.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.orderservice.common.Payment;
import com.microservice.orderservice.common.TransactionRequest;
import com.microservice.orderservice.common.TransactionResponse;
import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public TransactionResponse placeOrder(@RequestBody TransactionRequest request) throws JsonProcessingException {

        return orderService.saveOrder(request);
    }

}
