package com.paypilot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bill_categories")
public class BillCategory {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long categoryId;

    @Column(
            name = "category_name",
            nullable = false,
            unique = true
    )
    private String categoryName;

    @Column(nullable = false)
    private Boolean active = true;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BillCategory() {
    }

    // =====================================================
    // CONSTRUCTOR WITH PARAMETERS
    // =====================================================

    public BillCategory(
            Long categoryId,
            String categoryName,
            Boolean active) {

        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.active = active;
    }

    // =====================================================
    // GET CATEGORY ID
    // =====================================================

    public Long getCategoryId() {

        return categoryId;
    }

    // =====================================================
    // SET CATEGORY ID
    // =====================================================

    public void setCategoryId(
            Long categoryId) {

        this.categoryId = categoryId;
    }

    // =====================================================
    // GET CATEGORY NAME
    // =====================================================

    public String getCategoryName() {

        return categoryName;
    }

    // =====================================================
    // SET CATEGORY NAME
    // =====================================================

    public void setCategoryName(
            String categoryName) {

        this.categoryName = categoryName;
    }

    // =====================================================
    // GET ACTIVE
    // =====================================================

    public Boolean getActive() {

        return active;
    }

    // =====================================================
    // SET ACTIVE
    // =====================================================

    public void setActive(
            Boolean active) {

        this.active = active;
    }
}
