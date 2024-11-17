package com.learning;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ReadingReportService {
    private static final Path SOURCE_RELATORIO = new File("src/main/resources/relatorio.txt").toPath();

    public static void main(String[] args) {
        ReadingReportService readingReportService = new ReadingReportService();

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

    private void parse(ConsumerRecord<String, User> rec) throws IOException {
        System.out.println("------------------------------------------------------------");
        System.out.println(rec.topic());
        System.out.println(rec.key());

        User user = rec.value();
        System.out.println("User: " + user.getId());

        File target = new File(user.getReportPath());
        IO.copyTo(SOURCE_RELATORIO, target);
        IO.append(target, "Created for "+ user.getId());
    }
}
