package com.example;

import java.util.Map;
import java.util.List;
import java.io.IOException;

import com.example.handler.Producer;
import com.example.service.cryptoImpl.ETH;
import com.example.common.Logging;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import com.example.db.ClickhouseDriver;

public class App 
{
    public static void main( String[] args ) throws IOException, SQLException
    {
        Logging.info_message("Application starting...");

        // 1. Initialize Kafka Producer
        Producer kafkaProducer = new Producer();

        // 2. Fetch cryptocurrency price data
        ETH cryptoInstance = new ETH();
        Map<String, Object> priceData = cryptoInstance.getPriceData();
        
        if (priceData != null && !priceData.isEmpty()) {
            Logging.info_message("Successfully fetched ETH price data: " + priceData.toString());

            // 3. Convert data to JSON string
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(priceData);

            // 4. Send data to Kafka
            String topic = "test-topic";
            String key = "ETH"; // Use the currency symbol as the key
            kafkaProducer.sendMessage(topic, key, jsonMessage);

        } else {
            Logging.warn_message("Could not fetch ETH price data. No message sent.");
        }

        // 5. Close the producer before exiting
        // In a real, long-running application, you'd call this on shutdown.
        try {
            // Wait a moment for the async send to complete before closing.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Logging.error_message("Thread interrupted while waiting for Kafka send.");
            Thread.currentThread().interrupt();
        }
        kafkaProducer.close();
        Logging.info_message("Application finished.");
    }
}
