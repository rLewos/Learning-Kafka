package com.learning;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public class FraudDetectionService {
    public static void main(String[] args) {

        var fraudObj = new FraudDetectionService();
        String groupId = FraudDetectionService.class.getName();
        String topicName = "ECOMMERCE_NEW_ORDER";

        try(var service = new KafkaService<Order>(
                groupId
                , topicName
                , fraudObj::parse
                , Order.class.getName()
                , GsonDeserializer.class.getName())) {

            service.run();
        }
    }

    private final KafkaDispacher<Order> kafkaDispacher = new KafkaDispacher<>();

    private void parse(ConsumerRecord<String, Order> rec) {
        System.out.println("------------------------------------------------------------");
        System.out.println(rec.topic());
        System.out.println(rec.key());

        var order = rec.value();
        System.out.println(order.getName());

        try {
            Thread.sleep(500);

            if (isFraud(order)){
                System.out.println("Fraud has been detected! | " + order.toString());

                try {
                    kafkaDispacher.send("ECOMMERCE_NEW_ORDER_REJECTED", order.getEmail(), order);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }else{

                try {
                    kafkaDispacher.send("ECOMMERCE_NEW_ORDER_ACCEPTED", order.getEmail(), order);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Order has been done");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isFraud(Order order) {
        return order.getValue().compareTo(new BigDecimal(3500)) >= 0;
    }
}
