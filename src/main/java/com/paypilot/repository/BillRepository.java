package com.paypilot.repository;

import com.paypilot.entity.Bill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository
        extends JpaRepository<Bill, Long> {

    List<Bill> findByUserId(
            String userId
    );

    List<Bill>
    findByUserIdAndCategory_CategoryId(
            String userId,
            Long categoryId
    );

    List<Bill>
    findByUserIdAndPaid(
            String userId,
            Boolean paid
    );

    List<Bill>
    findByUserIdAndCategory_CategoryIdAndPaid(
            String userId,
            Long categoryId,
            Boolean paid
    );
}
