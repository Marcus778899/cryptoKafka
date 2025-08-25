package com.example.service.handler;

import java.util.Properties;
import com.example.config.Config;
import com.example.common.Logging;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import java.time.Duration;

public class Consumer {
    private final KafkaConsumer<String, String> consumer;

    public Consumer(String topic){
        // The comsuerProps field is already initialized when the object is created.
        // This constructor can be used for other setup tasks if needed.
        try{
            Logging.info_message("Initializing Kafka Comsuser...");
            Properties comsuerProps = Config.getConsumerProperties();
            this.consumer = new KafkaConsumer<>(comsuerProps);
            Logging.info_message("Kafka Comsuser initialized successfully.");
            this.consumer.subscribe(java.util.Collections.singletonList(topic));
        }
        catch(Exception e){
            Logging.error_message("Error initializing Kafka Comsuser: " + e.getMessage());
            throw e;
        }
    }

    public void consumeMessage(){
        try{
            while(true){
                ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
                for(org.apache.kafka.clients.consumer.ConsumerRecord<String,String> record : records){
                    Logging.info_message(
                        String.format(
                            "Consumed message: Key: %s Value: %s Partition: %d Offset: %d",
                            record.key(),
                            record.value(),
                            record.partition(),
                            record.offset()
                        )
                    );
                }
            }
        } catch (Exception e) {
            Logging.error_message("Error consuming messages: " + e.getMessage());
        } 
    }

    public void closeConsumer(){
        if(consumer != null){
            consumer.close();
            Logging.info_message("Kafka Comsuser closed successfully.");
        }
    }

    public static void main(String[] args) {
        Consumer action = new Consumer("test-topic");
        action.consumeMessage();
        action.closeConsumer();
    }

}   
