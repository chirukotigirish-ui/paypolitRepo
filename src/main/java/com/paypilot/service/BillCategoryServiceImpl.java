package com.paypilot.service;

import com.paypilot.entity.BillCategory;

import com.paypilot.exception
        .BillCategoryNotFoundException;

import com.paypilot.repository
        .BillCategoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillCategoryServiceImpl
        implements BillCategoryService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    BillCategoryServiceImpl.class
            );

    private final BillCategoryRepository
            billCategoryRepository;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BillCategoryServiceImpl(
            BillCategoryRepository
                    billCategoryRepository) {

        this.billCategoryRepository =
                billCategoryRepository;

        logger.info(
                "BillCategoryServiceImpl initialized"
        );
    }

    // =====================================================
    // GET ALL CATEGORIES
    // =====================================================

    @Override
    public List<BillCategory>
    getAllCategories() {

        logger.info(
                "Fetching all bill categories"
        );

        List<BillCategory> categories =
                billCategoryRepository.findAll();

        logger.info(
                "Total categories: {}",
                categories.size()
        );

        return categories;
    }

    // =====================================================
    // GET ACTIVE CATEGORIES
    // =====================================================

    @Override
    public List<BillCategory>
    getActiveCategories() {

        logger.info(
                "Fetching active bill categories"
        );

        List<BillCategory> categories =
                billCategoryRepository
                        .findByActiveTrue();

        logger.info(
                "Active categories found: {}",
                categories.size()
        );

        return categories;
    }

    // =====================================================
    // GET CATEGORY BY ID
    // =====================================================

    @Override
    public BillCategory getCategoryById(
            Long categoryId) {

        logger.info(
                "Fetching category ID: {}",
                categoryId
        );

        return billCategoryRepository
                .findById(categoryId)
                .orElseThrow(() -> {

                    logger.info(
                            "Category not found: {}",
                            categoryId
                    );

                    return new
                            BillCategoryNotFoundException(
                                    categoryId
                            );
                });
    }

    // =====================================================
    // ADD CATEGORY
    // =====================================================

    @Override
    public BillCategory addCategory(
            String categoryName) {

        logger.info(
                "Adding new category: {}",
                categoryName
        );

        BillCategory category =
                new BillCategory();

        category.setCategoryName(
                categoryName
        );

        category.setActive(true);

        BillCategory savedCategory =
                billCategoryRepository.save(
                        category
                );

        logger.info(
                "Category created successfully with ID: {}",
                savedCategory.getCategoryId()
        );

        return savedCategory;
    }

    // =====================================================
    // DEACTIVATE CATEGORY
    // =====================================================

    @Override
    public void deactivateCategory(
            Long categoryId) {

        logger.info(
                "Deactivating category: {}",
                categoryId
        );

        BillCategory category =
                getCategoryById(categoryId);

        category.setActive(false);

        billCategoryRepository.save(
                category
        );

        logger.info(
                "Category deactivated successfully: {}",
                categoryId
        );
    }
}
