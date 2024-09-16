package com.learning.Consumers;

import com.learning.Entities.Order;
import com.learning.KafkaService;
import com.learning.GsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectionService {
    public static void main(String[] args) {

        var fraudObj = new FraudDetectionService();
        String groupId = FraudDetectionService.class.getName();
        String topicName = "ECOMMERCE_NEW_ORDER";
        try(var service = new KafkaService<Order>(groupId, topicName, fraudObj::parse, Order.class.getName(), GsonDeserializer.class.getName())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> rec) {
        System.out.println("------------------------------------------------------------");
        System.out.println(rec.topic());
        System.out.println(rec.key());
        System.out.println(rec.value().getName());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Order has been done");
    }
}
