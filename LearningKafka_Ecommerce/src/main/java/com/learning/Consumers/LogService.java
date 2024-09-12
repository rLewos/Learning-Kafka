package com.learning.Consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {

        var consumer = new KafkaConsumer<String, String>(properties());

/*
        var topicsNameList = new ArrayList<String>();
        topicsNameList.add("ECOMMERCE_SEND_EMAIL");
        topicsNameList.add("ECOMMERCE_NEW_ORDER");
*/
        consumer.subscribe(Pattern.compile("ECOMMERCE.*"));

        System.out.println(LogService.class.getSimpleName() + " consumer is running.");
        while(true){
            var records = consumer.poll(Duration.ofMillis(100));
            for (var rec : records){
                System.out.println("------------------------------------------------------------");
                System.out.println(rec.topic());
                System.out.println(rec.key());
                System.out.println(rec.value());

                System.out.println("Log has been saved");
            }
        }
    }

    private static Properties properties(){
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());

        return properties;
    }
}
