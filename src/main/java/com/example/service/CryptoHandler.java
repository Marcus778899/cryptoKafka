package com.example.service;

import java.io.IOException;
import java.util.Map;

public interface CryptoHandler {
    /*
     * crapes the price data for a specific cryptocurrency.
     * @return A JSON formatted string with the price data.
     * @throws IOException If the scraping fails.
     */

    String fetchRawData() throws IOException;

    Map<String, Object> parseData(String rawData) throws IOException;

    default Map<String, Object> getPriceData() throws IOException {
        String raw = fetchRawData();
        return parseData(raw);
    }
}
