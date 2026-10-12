package com.paypilot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaypilotApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    PaypilotApplication.class
            );

    public static void main(String[] args) {

        logger.info(
                "Starting PayPilot Application"
        );

        SpringApplication.run(
                PaypilotApplication.class,
                args
        );

        logger.info(
                "PayPilot Application started successfully"
        );
    }
}
