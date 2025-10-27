package com.adil.bridgespero;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BridgeSperoApplication {

    @Value("${zoom.client-id}")
    private static String zoomClientId;

    public static void main(String[] args) {
        SpringApplication.run(BridgeSperoApplication.class, args);
        System.out.println(zoomClientId);
    }

}
