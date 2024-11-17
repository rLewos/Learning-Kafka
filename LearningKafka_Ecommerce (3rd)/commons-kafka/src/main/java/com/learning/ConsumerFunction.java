package com.learning;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.SQLException;

public interface ConsumerFunction<T> {
    void consume(ConsumerRecord<String, T> record) throws SQLException, IOException;
}
