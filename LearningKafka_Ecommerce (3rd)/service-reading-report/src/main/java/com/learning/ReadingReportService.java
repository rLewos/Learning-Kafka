package com.learning;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ReadingReportService {
    public static void main(String[] args) {

        var readingReportService = new ReadingReportService();

        String groupId = ReadingReportService.class.getName();
        String topicName = "USER_READING_REPORT";

        try(var service = new KafkaService<User>(
                groupId
                , topicName
                , readingReportService::parse
                , User.class.getName()
                , GsonDeserializer.class.getName())) {

            service.run();
        }
    }

    private void parse(ConsumerRecord<String, User> rec) {
        System.out.println("------------------------------------------------------------");
        System.out.println(rec.topic());
        System.out.println(rec.key());
    }
}
