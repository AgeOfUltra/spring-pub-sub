package com.pubsub.springpubsub.listeners;

import com.pubsub.springpubsub.events.OrderCreatedEvents;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LoggingOrderListener {

    @EventListener
    @Order(1) //
    @Async
    public void auditOrder(OrderCreatedEvents events) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Order is places successfully for Id "+events.getOrderId());

    }
}
