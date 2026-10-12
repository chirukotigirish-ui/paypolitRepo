package com.paypilot.service;

import com.paypilot.entity.BillCategory;

import java.util.List;

public interface BillCategoryService {

    List<BillCategory>
    getAllCategories();

    List<BillCategory>
    getActiveCategories();

    BillCategory getCategoryById(
            Long categoryId
    );

    BillCategory addCategory(
            String categoryName
    );

    void deactivateCategory(
            Long categoryId
    );
}