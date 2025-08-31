package com.pubsub.springpubsub.service;

import com.pubsub.springpubsub.events.OrderCreatedEvents;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public  void createOrder(String orderId,double amount){
        System.out.println("order is created "+orderId);
        publisher.publishEvent(new OrderCreatedEvents(orderId,amount));
    }
}
