package com.example.config;

import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.load();

    public static Properties getProducerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", dotenv.get("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092"));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // Add any other producer-specific properties from your .env file
        // Example: props.put("acks", dotenv.get("KAFKA_PRODUCER_ACKS", "all"));
        return props;
    }

    public static Properties getConsumerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", dotenv.get("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092"));
        props.put("group.id", dotenv.get("KAFKA_CONSUMER_GROUP_ID", "my-group"));
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        // Add any other consumer-specific properties from your .env file
        // Example: props.put("enable.auto.commit", dotenv.get("KAFKA_CONSUMER_ENABLE_AUTO_COMMIT", "true"));
        return props;
    }

    public static Properties getETHProperties(){
        Properties props = new Properties();
        props.put("eth.apiKey", dotenv.get("ETHERSCAN_TOKEN"));
        return props;
    }
}