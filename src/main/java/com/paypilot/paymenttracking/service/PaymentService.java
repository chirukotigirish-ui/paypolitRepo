package com.paypilot.paymenttracking.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.paypilot.paymenttracking.model.Payment;
import com.paypilot.paymenttracking.repository.PaymentRepository;

@Service
public class PaymentService {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Payment Progress
    public List<Payment> getPaymentProgress() {

        logger.info("Fetching payment progress");

        List<Payment> payments =
                paymentRepository.findAll();

        logger.info(
                "Payment progress fetched successfully. Count: {}",
                payments.size());

        return payments;
    }

    // Payment Overview
    public List<Payment> getPaymentOverview(
            String category,
            LocalDate fromDate,
            LocalDate toDate) {

        logger.info(
                "Fetching payment overview. Category: {}, From: {}, To: {}",
                category,
                fromDate,
                toDate);

        List<Payment> payments;

        if (category == null
                || category.trim().isEmpty()
                || category.equalsIgnoreCase("All")) {

            payments =
                    paymentRepository.findByDueDateBetween(
                            fromDate,
                            toDate);

        } else {

            payments =
                    paymentRepository
                            .findByBillCategoryAndDueDateBetween(
                                    category,
                                    fromDate,
                                    toDate);
        }

        logger.info(
                "Payment overview fetched successfully. Count: {}",
                payments.size());

        return payments;
    }

    // Payment History
    public List<Payment> getPaymentHistory(
            String category,
            LocalDate fromDate,
            LocalDate toDate) {

        logger.info(
                "Fetching payment history. Category: {}, From: {}, To: {}",
                category,
                fromDate,
                toDate);

        List<Payment> payments;

        if (category == null
                || category.trim().isEmpty()
                || category.equalsIgnoreCase("All")) {

            payments =
                    paymentRepository.findPaymentHistory(
                            fromDate,
                            toDate);

        } else {

            payments =
                    paymentRepository.findPaymentHistoryByCategory(
                            category,
                            fromDate,
                            toDate);
        }

        logger.info(
                "Payment history fetched successfully. Count: {}",
                payments.size());

        return payments;
    }
}