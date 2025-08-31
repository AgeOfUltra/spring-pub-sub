package com.pubsub.springpubsub.controller;

import com.pubsub.springpubsub.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public  String placeOrder(@RequestParam String orderId,@RequestParam double amount){
        orderService.createOrder(orderId,amount);
        return "Order placed successfully "+orderId;
    }

}
