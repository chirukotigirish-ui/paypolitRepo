package com.paypilot.paymenttracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentTrackingApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentTrackingApplication.class);

    public static void main(String[] args) {

        logger.info("Starting PayPilot Payment Tracking application");

        SpringApplication.run(
                PaymentTrackingApplication.class,
                args);

        logger.info("PayPilot Payment Tracking application started successfully");
    }
}