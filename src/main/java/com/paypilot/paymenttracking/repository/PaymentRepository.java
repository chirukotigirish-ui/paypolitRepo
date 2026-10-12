package com.paypilot.paymenttracking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.paypilot.paymenttracking.model.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findByDueDateBetween(
            LocalDate fromDate,
            LocalDate toDate);

    List<Payment> findByBillCategoryAndDueDateBetween(
            String billCategory,
            LocalDate fromDate,
            LocalDate toDate);

    @Query("SELECT p FROM Payment p " +
           "WHERE p.paymentStatus = 'COMPLETED' " +
           "AND p.paidDate BETWEEN :fromDate AND :toDate")
    List<Payment> findPaymentHistory(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);

    @Query("SELECT p FROM Payment p " +
           "WHERE p.paymentStatus = 'COMPLETED' " +
           "AND p.billCategory = :category " +
           "AND p.paidDate BETWEEN :fromDate AND :toDate")
    List<Payment> findPaymentHistoryByCategory(
            @Param("category") String category,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);
}