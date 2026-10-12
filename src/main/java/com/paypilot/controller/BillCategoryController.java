package com.paypilot.controller;

import com.paypilot.entity.BillCategory;
import com.paypilot.service.BillCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill-categories")
public class BillCategoryController {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    BillCategoryController.class
            );

    private final BillCategoryService
            billCategoryService;

    public BillCategoryController(
            BillCategoryService billCategoryService) {

        this.billCategoryService =
                billCategoryService;

        logger.info(
                "BillCategoryController initialized"
        );
    }

    // =====================================================
    // GET ALL CATEGORIES
    // =====================================================

    @GetMapping
    public ResponseEntity<List<BillCategory>>
    getAllCategories() {

        logger.info(
                "GET /api/bill-categories"
        );

        List<BillCategory> categories =
                billCategoryService
                        .getAllCategories();

        logger.info(
                "Categories fetched successfully"
        );

        return ResponseEntity.ok(categories);
    }

    // =====================================================
    // GET ACTIVE CATEGORIES
    // =====================================================

    @GetMapping("/active")
    public ResponseEntity<List<BillCategory>>
    getActiveCategories() {

        logger.info(
                "GET /api/bill-categories/active"
        );

        List<BillCategory> categories =
                billCategoryService
                        .getActiveCategories();

        logger.info(
                "Active categories fetched successfully"
        );

        return ResponseEntity.ok(categories);
    }

    // =====================================================
    // GET CATEGORY BY ID
    // =====================================================

    @GetMapping("/{categoryId}")
    public ResponseEntity<BillCategory>
    getCategoryById(
            @PathVariable Long categoryId) {

        logger.info(
                "GET /api/bill-categories/{}",
                categoryId
        );

        BillCategory category =
                billCategoryService
                        .getCategoryById(
                                categoryId
                        );

        logger.info(
                "Category fetched successfully: {}",
                categoryId
        );

        return ResponseEntity.ok(category);
    }

    // =====================================================
    // ADD CATEGORY
    // =====================================================

    @PostMapping
    public ResponseEntity<BillCategory>
    addCategory(
            @RequestParam String categoryName) {

        logger.info(
                "POST /api/bill-categories"
        );

        logger.info(
                "Adding category: {}",
                categoryName
        );

        BillCategory category =
                billCategoryService
                        .addCategory(
                                categoryName
                        );

        logger.info(
                "Category added successfully: {}",
                category.getCategoryId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(category);
    }

    // =====================================================
    // DEACTIVATE CATEGORY
    // =====================================================

    @PutMapping("/{categoryId}/deactivate")
    public ResponseEntity<String>
    deactivateCategory(
            @PathVariable Long categoryId) {

        logger.info(
                "PUT /api/bill-categories/{}/deactivate",
                categoryId
        );

        billCategoryService
                .deactivateCategory(
                        categoryId
                );

        logger.info(
                "Category deactivated successfully: {}",
                categoryId
        );

        return ResponseEntity.ok(
                "Category deactivated successfully"
        );
    }
}
