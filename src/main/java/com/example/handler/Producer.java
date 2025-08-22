package com.example.handler;

import java.util.Properties;
import com.example.config.Config;
import com.example.common.Logging;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Producer {

    private final KafkaProducer<String, String> producer;

    public Producer(){
        Logging.info_message("Initializing Kafka Producer...");
        Properties producerProps = Config.getProducerProperties();
        this.producer = new KafkaProducer<>(producerProps);
        Logging.info_message("Kafka Producer initialized successfully.");
    }

    /**
     * Sends a message to a specific Kafka topic.
     * @param topic The topic to send the message to.
     * @param key The key for the message, can be null.
     * @param value The message content.
     */
    public void sendMessage(String topic, String key, String value) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    Logging.info_message(String.format("Message sent to topic=%s, partition=%d, offset=%d",
                            metadata.topic(), metadata.partition(), metadata.offset()));
                } else {
                    Logging.error_message("Failed to send message: " + exception.getMessage());
                }
            });
        } catch (Exception e) {
            Logging.error_message("Error sending message: " + e.getMessage());
        }
    }

    /**
     * Closes the producer connection.
     */
    public void close() {
        Logging.info_message("Closing Kafka Producer...");
        producer.flush();
        producer.close();
        Logging.info_message("Kafka Producer closed.");
    }
}

