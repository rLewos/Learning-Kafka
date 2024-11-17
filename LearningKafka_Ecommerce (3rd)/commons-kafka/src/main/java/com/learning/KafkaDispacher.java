package com.learning;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispacher<T> implements Closeable {

    private KafkaProducer<String, T> producer;

    public KafkaDispacher(){
        this.producer = new KafkaProducer<>(properties());
    }

    public static Properties properties(){
        Properties p = new Properties();

        p.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        p.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        p.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        p.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // 0, 1(leader) and all(full-cluster-sync)

        return p;
    }

    public void send(String topic, String key, T value) throws ExecutionException, InterruptedException {
        var record = new ProducerRecord<String, T>(topic, key, value);

        Callback cb = (success, error)->{
            if (error != null){
                System.out.println("Error");
                return;
            }

            System.out.println("Topic that message has been sent: " + success.topic());
        };

        producer.send(record, cb).get();
    }

    @Override
    public void close(){
        producer.close();
    }
}
