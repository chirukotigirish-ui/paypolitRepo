package com.paypilot.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long billId;

    @Column(
            name = "user_id",
            nullable = false
    )
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private BillCategory category;

    @Column(
            name = "due_date",
            nullable = false
    )
    private LocalDate dueDate;

    @Column(
            nullable = false
    )
    private Double amount;

    @Column(
            name = "recurring_frequency"
    )
    private String recurringFrequency;

    @Column(
            nullable = false
    )
    private Boolean recurring = false;

    @Column(
            nullable = false
    )
    private Boolean paid = false;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Bill() {
    }

    // =====================================================
    // GET BILL ID
    // =====================================================

    public Long getBillId() {

        return billId;
    }

    // =====================================================
    // SET BILL ID
    // =====================================================

    public void setBillId(
            Long billId) {

        this.billId = billId;
    }

    // =====================================================
    // GET USER ID
    // =====================================================

    public String getUserId() {

        return userId;
    }

    // =====================================================
    // SET USER ID
    // =====================================================

    public void setUserId(
            String userId) {

        this.userId = userId;
    }

    // =====================================================
    // GET CATEGORY
    // =====================================================

    public BillCategory getCategory() {

        return category;
    }

    // =====================================================
    // SET CATEGORY
    // =====================================================

    public void setCategory(
            BillCategory category) {

        this.category = category;
    }

    // =====================================================
    // GET DUE DATE
    // =====================================================

    public LocalDate getDueDate() {

        return dueDate;
    }

    // =====================================================
    // SET DUE DATE
    // =====================================================

    public void setDueDate(
            LocalDate dueDate) {

        this.dueDate = dueDate;
    }

    // =====================================================
    // GET AMOUNT
    // =====================================================

    public Double getAmount() {

        return amount;
    }

    // =====================================================
    // SET AMOUNT
    // =====================================================

    public void setAmount(
            Double amount) {

        this.amount = amount;
    }

    // =====================================================
    // GET RECURRING FREQUENCY
    // =====================================================

    public String getRecurringFrequency() {

        return recurringFrequency;
    }

    // =====================================================
    // SET RECURRING FREQUENCY
    // =====================================================

    public void setRecurringFrequency(
            String recurringFrequency) {

        this.recurringFrequency =
                recurringFrequency;
    }

    // =====================================================
    // GET RECURRING
    // =====================================================

    public Boolean getRecurring() {

        return recurring;
    }

    // =====================================================
    // SET RECURRING
    // =====================================================

    public void setRecurring(
            Boolean recurring) {

        this.recurring = recurring;
    }

    // =====================================================
    // GET PAID
    // =====================================================

    public Boolean getPaid() {

        return paid;
    }

    // =====================================================
    // SET PAID
    // =====================================================

    public void setPaid(
            Boolean paid) {

        this.paid = paid;
    }
}