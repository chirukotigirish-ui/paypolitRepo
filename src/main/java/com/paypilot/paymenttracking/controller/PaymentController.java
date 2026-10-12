package com.paypilot.paymenttracking.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypilot.paymenttracking.model.Payment;
import com.paypilot.paymenttracking.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Payment Progress
    @GetMapping("/progress")
    public List<Payment> getPaymentProgress() {

        logger.info(
                "Received request for payment progress");

        return paymentService.getPaymentProgress();
    }

    // Payment Overview
    @GetMapping("/overview")
    public List<Payment> getPaymentOverview(
            @RequestParam String category,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        logger.info(
                "Received payment overview request. " +
                "Category: {}, From: {}, To: {}",
                category,
                fromDate,
                toDate);

        LocalDate from =
                LocalDate.parse(fromDate);

        LocalDate to =
                LocalDate.parse(toDate);

        return paymentService.getPaymentOverview(
                category,
                from,
                to);
    }

    // Payment History
    @GetMapping("/history")
    public List<Payment> getPaymentHistory(
            @RequestParam String category,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        logger.info(
                "Received payment history request. " +
                "Category: {}, From: {}, To: {}",
                category,
                fromDate,
                toDate);

        LocalDate from =
                LocalDate.parse(fromDate);

        LocalDate to =
                LocalDate.parse(toDate);

        return paymentService.getPaymentHistory(
                category,
                from,
                to);
    }
}