package com.ecom.notification;


import com.ecom.notification.payload.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class OrderEventConsumer {
//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOrderEvent(OrderCreatedEvent orderEvent){
//
//        System.out.println("Received order eventupdated " + orderEvent);
//
//
//    }

    @Bean
    public Consumer<OrderCreatedEvent> orderCreated(){

        return event -> {
            log.info("Received order created event for order: {}", event.getOrderId());
            log.info("Received order created event for order: {}", event.getUserId());
        };

    }
}
