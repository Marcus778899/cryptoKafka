package com.example.service.cryptoImpl;

import com.example.config.Config;
import com.example.service.CryptoHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.common.Logging;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ETH implements CryptoHandler{

    Properties ETHSetting = Config.getETHProperties();

    private final String sourceUrl = String.format(
        "https://api.etherscan.io/api?module=stats&action=ethprice&apikey=%s",
        ETHSetting.getProperty("eth.apiKey")
        );

    @Override
    public String fetchRawData() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(sourceUrl); // Moved inside try block
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            if (status != 200) {
                Logging.warn_message("HTTP Error code: " + status);
                return null;
            }

           Logging.info_message("Response Status code is " + status);
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = input.readLine()) != null) {
                content.append(inputLine);
            }
            input.close();

            return content.toString();
        } catch (IOException e) {
            Logging.error_message("Error Occurred: " + e.getMessage());
            return null; // Return null on exception
        } finally {
            if (conn != null) {
                conn.disconnect(); // Ensure connection is always closed
            }
        }
    }

    @Override
    public Map<String, Object> parseData(String rawdata) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(
            rawdata,
            mapper.getTypeFactory().constructMapType(
                HashMap.class,
                String.class, 
                Object.class
                )
            );
        return result;
    }
}
