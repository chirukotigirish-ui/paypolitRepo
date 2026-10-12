package com.paypilot.repository;

import com.paypilot.entity.BillCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillCategoryRepository
        extends JpaRepository<BillCategory, Long> {

    Optional<BillCategory>
    findByCategoryName(
            String categoryName
    );

    List<BillCategory>
    findByActiveTrue();
}
