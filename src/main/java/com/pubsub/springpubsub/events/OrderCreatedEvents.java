package com.pubsub.springpubsub.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreatedEvents {
    private final String orderId;
    private double amount;
}
