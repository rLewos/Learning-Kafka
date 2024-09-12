package com.learning.Producers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

public class FraudDetectionService {
    public static void main(String[] args) {

        var consumer = new KafkaConsumer<String, String>(properties());
        var topicsNameList = new ArrayList<String>();
        topicsNameList.add("ECOMMERCE_NEW_ORDER");
        consumer.subscribe(topicsNameList);

        System.out.println(FraudDetectionService.class.getSimpleName() + " consumer is running.");
        while(true){
            var records = consumer.poll(Duration.ofMillis(100));
            for (var rec : records){
                System.out.println("------------------------------------------------------------");
                System.out.println(rec.topic());
                System.out.println(rec.key());
                System.out.println(rec.value());

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Order has been done");
            }
        }
    }

    private static Properties properties(){
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectionService.class.getSimpleName());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1"); // Puxa uma mensagem por vez a cada consumer poll. Evita problemas em caso de rebalanceamento dos consumers em topics.

        return properties;
    }
}
