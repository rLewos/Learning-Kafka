package com.learning.Producers;

import com.learning.Entities.Email;
import com.learning.Entities.Order;
import com.learning.KafkaDispacher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain{
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try(var dispacherOrder = new KafkaDispacher<Order>()) {
            try(var dispacherEmail = new KafkaDispacher<Email>()){
                String queueName = "ECOMMERCE_NEW_ORDER";
                String queueNameEmail = "ECOMMERCE_SEND_EMAIL";

                for (int i = 0; i < 3; i++) {
                    String keyMessage = UUID.randomUUID().toString();
                    //String message = "123,456,78999";

                    String messageEmailKey = UUID.randomUUID().toString();
                    //String messageEmail = "(" + i + ")" + "Thanks!";
                    Email email = new Email("(" + i + ")" + " BananaPhone Order", "Thanks");
                    Order order = new Order(UUID.randomUUID().toString(), "(" + i + ")" + " Banana", BigDecimal.valueOf(1));

                    dispacherOrder.send(queueName, keyMessage, order);
                    dispacherEmail.send(queueNameEmail, messageEmailKey, email);
                    System.out.println("Order has been sent.");
                }
            }
        };

    }
}