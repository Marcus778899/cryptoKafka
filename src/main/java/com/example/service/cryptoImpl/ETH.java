package com.example.service.cryptoImpl;

import com.example.config.Config;
import com.example.service.CryptoHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.common.Logging;
import com.example.schema.Response;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Map;
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
        Response.ETHResponse result = mapper.readValue(rawdata, Response.ETHResponse.class);
        if(!"1".equals(result.status) || !"OK".equals(result.message)){
            Logging.error_message("API Error: " + result.message);
            throw new IOException("API Error: " + result.message);
        }
        else{
            Logging.info_message("API Success");
            return mapper.convertValue(
                result.result, 
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {}
                );
        }
    }
}
