package com.paypilot.controller;

import dto.BillRequest;
import dto.BillResponse;
import com.paypilot.service.BillService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    BillController.class
            );

    private final BillService billService;

    public BillController(
            BillService billService) {

        this.billService =
                billService;

        logger.info(
                "BillController initialized"
        );
    }

    // =====================================================
    // ADD BILL
    // =====================================================

    @PostMapping
    public ResponseEntity<BillResponse>
    addBill(
            @Valid @RequestBody BillRequest request) {

        logger.info(
                "POST /api/bills - Adding bill for user: {}",
                request.userId()
        );

        BillResponse response =
                billService.addBill(
                        request
                );

        logger.info(
                "POST /api/bills - Bill added successfully"
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =====================================================
    // GET ALL BILLS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<BillResponse>>
    getAllBills() {

        logger.info(
                "GET /api/bills - Fetching all bills"
        );

        List<BillResponse> response =
                billService.getAllBills();

        logger.info(
                "GET /api/bills - Bills fetched successfully"
        );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
    // GET BILL BY ID
    // =====================================================

    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse>
    getBillById(
            @PathVariable Long billId) {

        logger.info(
                "GET /api/bills/{}",
                billId
        );

        BillResponse response =
                billService.getBillById(
                        billId
                );

        logger.info(
                "Bill fetched successfully: {}",
                billId
        );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
    // UPDATE BILL
    // =====================================================

    @PutMapping("/{billId}")
    public ResponseEntity<BillResponse>
    updateBill(
            @PathVariable Long billId,
            @Valid @RequestBody BillRequest request) {

        logger.info(
                "PUT /api/bills/{} - Updating bill",
                billId
        );

        BillResponse response =
                billService.updateBill(
                        billId,
                        request
                );

        logger.info(
                "Bill updated successfully: {}",
                billId
        );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
    // DELETE BILL
    // =====================================================

    @DeleteMapping("/{billId}")
    public ResponseEntity<String>
    deleteBill(
            @PathVariable Long billId) {

        logger.info(
                "DELETE /api/bills/{}",
                billId
        );

        billService.deleteBill(
                billId
        );

        logger.info(
                "Bill deleted successfully: {}",
                billId
        );

        return ResponseEntity.ok(
                "Bill deleted successfully"
        );
    }

    // =====================================================
    // MARK BILL AS PAID
    // =====================================================

    @PutMapping("/{billId}/paid")
    public ResponseEntity<BillResponse>
    markBillAsPaid(
            @PathVariable Long billId) {

        logger.info(
                "PUT /api/bills/{}/paid",
                billId
        );

        BillResponse response =
                billService.markBillAsPaid(
                        billId
                );

        logger.info(
                "Bill marked as paid: {}",
                billId
        );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
    // MARK BILL AS UNPAID
    // =====================================================

    @PutMapping("/{billId}/unpaid")
    public ResponseEntity<BillResponse>
    markBillAsUnpaid(
            @PathVariable Long billId) {

        logger.info(
                "PUT /api/bills/{}/unpaid",
                billId
        );

        BillResponse response =
                billService.markBillAsUnpaid(
                        billId
                );

        logger.info(
                "Bill marked as unpaid: {}",
                billId
        );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
    // GET BILLS BY USER
    // =====================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BillResponse>>
    getBillsByUser(
            @PathVariable String userId) {

        logger.info(
                "GET /api/bills/user/{}",
                userId
        );

        List<BillResponse> response =
                billService.getBillsByUser(
                        userId
                );

        logger.info(
                "Bills fetched for user: {}",
                userId
        );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
    // GET BILLS BY USER + CATEGORY
    // =====================================================

    @GetMapping(
            "/user/{userId}/category/{categoryId}"
    )
    public ResponseEntity<List<BillResponse>>
    getBillsByUserAndCategory(
            @PathVariable String userId,
            @PathVariable Long categoryId) {

        logger.info(
                "Fetching bills for user {} and category {}",
                userId,
                categoryId
        );

        List<BillResponse> response =
                billService
                        .getBillsByUserAndCategory(
                                userId,
                                categoryId
                        );

        logger.info(
                "Bills fetched for user {} and category {}",
                userId,
                categoryId
        );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
    // GET BILLS BY USER + PAYMENT STATUS
    // =====================================================

    @GetMapping(
            "/user/{userId}/status/{paid}"
    )
    public ResponseEntity<List<BillResponse>>
    getBillsByUserAndStatus(
            @PathVariable String userId,
            @PathVariable Boolean paid) {

        logger.info(
                "Fetching bills for user {} with paid status {}",
                userId,
                paid
        );

        List<BillResponse> response =
                billService
                        .getBillsByUserAndStatus(
                                userId,
                                paid
                        );

        logger.info(
                "Bills fetched for user {} with status {}",
                userId,
                paid
        );

        return ResponseEntity.ok(
                response
        );
    }

    // =====================================================
    // GET USER + CATEGORY + STATUS
    // =====================================================

    @GetMapping(
            "/user/{userId}/category/{categoryId}/status/{paid}"
    )
    public ResponseEntity<List<BillResponse>>
    getBillsByUserCategoryAndStatus(
            @PathVariable String userId,
            @PathVariable Long categoryId,
            @PathVariable Boolean paid) {

        logger.info(
                "Fetching bills for user {}, category {}, status {}",
                userId,
                categoryId,
                paid
        );

        List<BillResponse> response =
                billService
                        .getBillsByUserCategoryAndStatus(
                                userId,
                                categoryId,
                                paid
                        );

        logger.info(
                "Bills fetched successfully"
        );

        return ResponseEntity.ok(
                response
        );
    }
}
