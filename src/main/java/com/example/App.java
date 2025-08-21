package com.example;

import java.util.Map;
import java.io.IOException;

import com.example.handler.Comsuer;
import com.example.service.impl.ETH;;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        Comsuer myConsumer = new Comsuer();

        myConsumer.printEnv();

        ETH cryptoInstance = new ETH();
        Map<String, Object> urlValue = cryptoInstance.getPriceData();
        
        for ( String key : urlValue.keySet()){
            String message = String.format("The %s is %s", key, urlValue.get(key));
            System.out.println(message);
        }
    }
}
