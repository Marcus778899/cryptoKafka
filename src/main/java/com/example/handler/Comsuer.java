package com.example.handler;

import java.util.Properties;
import com.example.config.Config;
import com.example.common.Logging;

public class Comsuer {
    Properties comsuerProps = Config.getConsumerProperties();

    public Comsuer(){
        // The comsuerProps field is already initialized when the object is created.
        // This constructor can be used for other setup tasks if needed.
    }

    public void printEnv(){
        Logging.info_message(comsuerProps.toString());
    }
}   
