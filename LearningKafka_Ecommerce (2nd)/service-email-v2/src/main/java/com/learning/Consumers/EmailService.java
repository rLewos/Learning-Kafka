package com.learning.Consumers;

import com.learning.Entities.Email;
import com.learning.GsonDeserializer;
import com.learning.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {

    public static void main(String[] args) {
        var emailService = new EmailService();

        // This 'try' clause is quite close to 'using' from C#
        try(var service = new KafkaService(
                EmailService.class.getSimpleName()
                , "ECOMMERCE_SEND_EMAIL"
                , emailService::parse
                , Email.class.getName()
                , GsonDeserializer.class.getName()
        )){
            service.run();
        }
    }

    public void parse(ConsumerRecord<String, Email> rec){
        System.out.println("------------------------------------------------------------");
        System.out.println(rec.topic());
        System.out.println(rec.key());
        System.out.println(rec.value());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Email has been sent");
    }
}
