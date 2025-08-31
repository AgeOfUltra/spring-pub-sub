package com.pubsub.springpubsub.listeners;

import com.pubsub.springpubsub.events.OrderCreatedEvents;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailComponentLister {

    @EventListener
    @Order(2) // to control the order of execution
    @Async // to execute as parallel thread
    public  void handleOrderCreatedAndSendEmail(OrderCreatedEvents events) throws InterruptedException {
        Thread.sleep(1500);
        System.out.println("Sending email for Order "+events.getOrderId()+" with price ");

    }

}
