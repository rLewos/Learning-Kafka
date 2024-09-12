package com.learning.Consumers;

import com.learning.Entities.Order;
import com.learning.KafkaDispacher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain{
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try(var dispacher = new KafkaDispacher()) {
            String queueName = "ECOMMERCE_NEW_ORDER";
            String queueNameEmail = "ECOMMERCE_SEND_EMAIL";

            Order order = new Order(UUID.randomUUID().toString(), "Banana", BigDecimal.valueOf(1));

            for (int i = 0; i < 10; i++) {
                String keyMessage = UUID.randomUUID().toString();
                //String message = "123,456,78999";

                String messageEmailKey = UUID.randomUUID().toString();
                String messageEmail = "(" + i + ")" + "Thanks!";

                dispacher.send(queueName, keyMessage, order);
                dispacher.send(queueNameEmail, messageEmailKey, messageEmail);
            }
        };
    }
}