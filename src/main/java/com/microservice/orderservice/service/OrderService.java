package com.microservice.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.orderservice.common.Payment;
import com.microservice.orderservice.common.TransactionRequest;
import com.microservice.orderservice.common.TransactionResponse;
import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Service
@RefreshScope
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    @Lazy
    private RestTemplate restTemplate;
    @Value("${microservice.payment-service.endpoints.endpoint.uri}")
    private String ENDPOINT_URL;

    public TransactionResponse saveOrder(TransactionRequest request) throws JsonProcessingException {
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        log.info("OrderService request : {}", new ObjectMapper().writeValueAsString(request));
        // rest call
//        Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment",
//                payment,Payment.class);
        Payment paymentResponse = restTemplate.postForObject(ENDPOINT_URL,payment,Payment.class);
        log.info("Payment-service response from OrderService rest call : {}",
                new ObjectMapper().writeValueAsString(paymentResponse));
        String message = paymentResponse.getPaymentStatus().equals("success")?
                "payment processing successfull and order placed"
                :"there is a failure in payment api, order added to cart";

        orderRepository.save(order);
        return new TransactionResponse(order,
                paymentResponse.getAmount(),
                paymentResponse.getTransactionId(), message);
    }
}
