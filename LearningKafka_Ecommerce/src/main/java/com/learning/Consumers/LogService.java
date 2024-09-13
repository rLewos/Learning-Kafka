package com.learning.Consumers;

import com.learning.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {

        var logService = new LogService();
        Pattern topicName = Pattern.compile("ECOMMERCE.*");

        try(var service = new KafkaService(LogService.class.getName()
                , topicName
                , logService::parse
                , String.class.getName()
                , StringDeserializer.class.getName()
        )){
            service.run();
            System.out.println(LogService.class.getSimpleName() + " consumer is running.");
        }
    }

    private void parse(ConsumerRecord<String, String> rec) {
        System.out.println("------------------------------------------------------------");
        System.out.println(rec.topic());
        System.out.println(rec.key());
        System.out.println(rec.value());

        System.out.println("Log has been saved");
    }
}
