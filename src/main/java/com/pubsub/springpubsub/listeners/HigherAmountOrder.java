package com.pubsub.springpubsub.listeners;

import com.pubsub.springpubsub.events.OrderCreatedEvents;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HigherAmountOrder {
    @EventListener(condition = "#events.amount > 1000")
    public void placedHigherAmountOrder(OrderCreatedEvents events){
        System.out.println("High value order detected with Id :"+events.getOrderId()+" with amount "+events.getAmount());
        throw  new RuntimeException("Error occurred");
    }
}
