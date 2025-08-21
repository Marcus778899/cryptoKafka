package com.example.handler;

import java.util.Properties;
import com.example.config.Config;;

public class Comsuer {
    Properties comsuerProps = Config.getConsumerProperties();

    public Comsuer(){
        // The comsuerProps field is already initialized when the object is created.
        // This constructor can be used for other setup tasks if needed.
    }

    public void printEnv(){
        System.out.println(this.comsuerProps);
    }
}   
