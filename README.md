# Spring Pub-Sub (Events) — Publisher, Subscribers, Ordered, Async, and Conditional Listeners

This project demonstrates Spring's built-in publish/subscribe model using Application Events. It covers:

- Publisher using ApplicationEventPublisher
- Subscribers using @EventListener
- Ordered listener execution with @Order
- Asynchronous listener execution with @Async
- Conditional listeners using SpEL conditions

## Project Structure (Relevant Parts)

- `events/OrderCreatedEvents.java`: Event payload
- `service/OrderService.java`: Publishes events
- `listeners/LoggingOrderListener.java`: Ordered + Async listener
- `listeners/EmailComponentLister.java`: Ordered + Async listener
- `listeners/HigherAmountOrder.java`: Conditional listener (amount > 1000)

## 1) Event (The Payload)

```java
// src/main/java/com/pubsub/springpubsub/events/OrderCreatedEvents.java
@Data
@AllArgsConstructor
public class OrderCreatedEvents {
    private final String orderId;
    private double amount;
}
```

- Carries the data that listeners react to.

## 2) Publisher (Producer of Events)

```java
// src/main/java/com/pubsub/springpubsub/service/OrderService.java
@Service
public class OrderService {

    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void createOrder(String orderId, double amount) {
        System.out.println("order is created " + orderId);
        publisher.publishEvent(new OrderCreatedEvents(orderId, amount));
    }
}
```

- Uses `ApplicationEventPublisher` to publish `OrderCreatedEvents`.

## 3) Subscribers (Consumers via @EventListener)

### 3.1 Ordered + Async Listener

```java
// src/main/java/com/pubsub/springpubsub/listeners/LoggingOrderListener.java
@Component
public class LoggingOrderListener {

    @EventListener
    @Order(1)    // executes before @Order(2)
    @Async       // executes in a separate thread
    public void auditOrder(OrderCreatedEvents events) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Order is places successfully for Id " + events.getOrderId());
    }
}
```

```java
// src/main/java/com/pubsub/springpubsub/listeners/EmailComponentLister.java
@Component
public class EmailComponentLister {

    @EventListener
    @Order(2)   // executes after @Order(1)
    @Async      // executes in a separate thread
    public void handleOrderCreatedAndSendEmail(OrderCreatedEvents events) throws InterruptedException {
        Thread.sleep(1500);
        System.out.println("Sending email for Order " + events.getOrderId() + " with price ");
    }
}
```

- **Order of listener execution**: Lower `@Order` value runs first (1 before 2) when listeners are invoked in the same thread. When using `@Async`, each listener runs on a different thread (parallel), so ordering guarantees don’t strictly apply across threads. If strict ordering is required, avoid `@Async` or chain logic deliberately.

### 3.2 Conditional Listener

```java
// src/main/java/com/pubsub/springpubsub/listeners/HigherAmountOrder.java
@Component
public class HigherAmountOrder {
    @EventListener(condition = "#events.amount > 1000")
    public void placedHigherAmountOrder(OrderCreatedEvents events) {
        System.out.println("High value order detected with Id :" + events.getOrderId() +
            " with amount " + events.getAmount());
        throw new RuntimeException("Error occurred");
    }
}
```

- **Conditional listener**: Runs only when the SpEL condition evaluates to `true`. Here, only when `amount > 1000`.

## Enabling Async Execution

To use `@Async`, ensure you:

1. Add `@EnableAsync` to a configuration class (e.g., your main application class):

```java
// src/main/java/com/pubsub/springpubsub/SpringPubSubApplication.java
@SpringBootApplication
@EnableAsync
public class SpringPubSubApplication { /* ... */ }
```

2. Have a TaskExecutor available (Spring Boot auto-configures a `SimpleAsyncTaskExecutor`/`TaskExecutor` if `@EnableAsync` is used; customize by defining a `ThreadPoolTaskExecutor` bean if needed).

## How Event Flow Works (Theory)

1. **Publisher** creates and publishes an event object via `ApplicationEventPublisher#publishEvent`.
2. Spring’s **event multicaster** finds all beans with methods annotated with `@EventListener` that accept the event type (or a super type).
3. If `@Async` is present and `@EnableAsync` is active, the method executes on an async executor thread.
4. If `@Order` is present (and execution is synchronous), listeners are invoked in ascending order of the `@Order` value.
5. If a listener has a **condition**, it runs only when the condition evaluates to true.
6. Exceptions thrown by async listeners do not propagate to the publisher thread; log/handle them explicitly.

## Running the Demo

1. Start the Spring Boot application.
2. Trigger `OrderService#createOrder(orderId, amount)` (e.g., from a controller, runner, or test) with different amounts to see:
   - Ordered + async behavior for general events
   - Conditional handling when `amount > 1000`

## Notes

- With `@Async`, listeners execute concurrently. Do not rely on `@Order` for cross-thread sequencing.
- To enforce a strict sequence, either keep listeners synchronous or orchestrate explicitly inside a single listener.
- Handle exceptions in async listeners using try/catch or `AsyncUncaughtExceptionHandler`.