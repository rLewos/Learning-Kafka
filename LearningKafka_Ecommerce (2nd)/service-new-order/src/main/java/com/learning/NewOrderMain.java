package com.learning;

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
                    String messageEmailKey = UUID.randomUUID().toString();
                    Email email = new Email("(" + i + ")" + " BananaPhone Order", "Thanks");

                    String orderKey = UUID.randomUUID().toString();
                    String orderName = "(" + i + ")" + " Banana";
                    BigDecimal orderValue = new BigDecimal(Math.random() * 5000 + 1);
                    Order order = new Order(
                            orderKey
                            , orderName
                            , orderValue
                    );

                    dispacherOrder.send(queueName, keyMessage, order);
                    dispacherEmail.send(queueNameEmail, messageEmailKey, email);
                    
                    System.out.println("Order has been sent.");
                }
            }
        };

    }
}