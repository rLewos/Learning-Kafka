package com.learning;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String,T> consumer;
    private final ConsumerFunction parse;
    private final String groupId;
    private final String className;
    private final String deserializerClassName;

    public KafkaService(String groupId, String topic, ConsumerFunction parse, String className, String deserializerClassName){
        this(groupId, parse, className, deserializerClassName);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(String groupId, Pattern topicName, ConsumerFunction parse, String className, String deserializerClassName) {
        this(groupId, parse, className,deserializerClassName);
        consumer.subscribe(topicName);
    }

    private KafkaService(String groupId, ConsumerFunction parse, String className, String deserializerClassName){
        this.parse = parse;
        this.groupId = groupId;
        this.className = className;
        this.deserializerClassName = deserializerClassName;

        this.consumer = new KafkaConsumer<String, T>(properties(groupId));
    }

    public void run(){
        System.out.println(groupId + " consumer is running.");

        while(true){
            var records = consumer.poll(Duration.ofMillis(100));
            for (var rec : records){
                parse.consume(rec);
            }
        }
    }

    private Properties properties(String groupId){
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializerClassName);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, groupId + "-" + UUID.randomUUID().toString());
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, className);

        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
