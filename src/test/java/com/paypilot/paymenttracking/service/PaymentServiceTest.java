package com.paypilot.paymenttracking.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.paypilot.paymenttracking.model.Payment;
import com.paypilot.paymenttracking.repository.PaymentRepository;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentService paymentService;

    @Before
    public void setUp() {

        paymentService =
                new PaymentService(paymentRepository);
    }

    @Test
    public void testGetPaymentProgress() {

        Payment payment1 = new Payment();

        payment1.setPaymentId(1L);
        payment1.setBillCategory("Electricity");
        payment1.setPaymentStatus("COMPLETED");


        Payment payment2 = new Payment();

        payment2.setPaymentId(2L);
        payment2.setBillCategory("Water");
        payment2.setPaymentStatus("PENDING");


        List<Payment> payments =
                Arrays.asList(payment1, payment2);


        when(paymentRepository.findAll())
                .thenReturn(payments);


        List<Payment> result =
                paymentService.getPaymentProgress();


        assertEquals(2, result.size());

        assertEquals(
                "Electricity",
                result.get(0).getBillCategory());

        assertEquals(
                "PENDING",
                result.get(1).getPaymentStatus());
    }
}