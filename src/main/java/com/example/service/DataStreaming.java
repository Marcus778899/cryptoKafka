package com.example.service;

import com.example.common.Logging;
import com.example.service.cryptoImpl.CryptoInterface;
import com.example.service.cryptoImpl.ETH;
import com.example.service.handler.Producer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataStreaming {

    final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public Map<String, Object> priceScrapingCenter() throws IOException{
        Instant timeStamp = Instant.now();
        String timeString = sdf.format(new Date(timeStamp.toEpochMilli()));
        CryptoInterface ethData = new ETH();
        Map<String, Object> priceData = ethData.getPriceData();
        if (priceData != null) {
            Logging.info_message("API request successfully on " + timeString.toString());
            return priceData;
        } else {
            Logging.warn_message("No data found");
            return null;
        }
    }

    public void publishOnTopic(Map<String, Object> priceData, String topic, String event) throws IOException{
        // 1. Initialize Kafka Producer
        Producer kafkaProducer = new Producer();

        // 2. Convert data to JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(priceData);

        // 3. Send data to Kafka
        kafkaProducer.sendMessage(topic, event, jsonMessage);


        // 4. Close the producer before exiting
        // In a real, long-running application, you'd call this on shutdown.
        try {
            // Wait a moment for the async send to complete before closing.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Logging.error_message("Thread interrupted while waiting for Kafka send.");
            Thread.currentThread().interrupt();
        }
    }

    public void insertClickhouse(Map<String, Object> priceData){

    }
    public static void main(String[] args) throws IOException{
        DataStreaming instance = new DataStreaming();
        instance.priceScrapingCenter();
    }

}