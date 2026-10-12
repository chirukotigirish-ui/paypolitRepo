package com.paypilot.exception;

public class BillCategoryNotFoundException
        extends RuntimeException {

    public BillCategoryNotFoundException(
            Long categoryId) {

        super(
                "Bill category not found with ID: "
                        + categoryId
        );
    }
}
