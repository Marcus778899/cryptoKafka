package com.example.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class Response {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ETHResponse{
        public String status;
        public String message;
        public Result result;
        
        public static class Result {
            public String ethbtc;
            public String ethbtc_timestamp;
            public String ethusd;
            public String ethusd_timestamp;
        }
    }

    // Add the response body below
}
