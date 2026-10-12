package com.paypilot.service;

import dto.BillRequest;
import dto.BillResponse;

import com.paypilot.entity.Bill;
import com.paypilot.entity.BillCategory;

import com.paypilot.exception
        .BillCategoryNotFoundException;

import com.paypilot.exception
        .BillNotFoundException;

import com.paypilot.repository.BillCategoryRepository;
import com.paypilot.repository.BillRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl
        implements BillService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    BillServiceImpl.class
            );

    private final BillRepository billRepository;

    private final BillCategoryRepository
            billCategoryRepository;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BillServiceImpl(
            BillRepository billRepository,
            BillCategoryRepository
                    billCategoryRepository) {

        this.billRepository =
                billRepository;

        this.billCategoryRepository =
                billCategoryRepository;

        logger.info(
                "BillServiceImpl initialized"
        );
    }

    // =====================================================
    // ADD BILL
    // =====================================================

    @Override
    public BillResponse addBill(
            BillRequest request) {

        logger.info(
                "Adding new bill for user: {}",
                request.userId()
        );

        logger.info(
                "Searching category ID: {}",
                request.categoryId()
        );

        BillCategory category =
                billCategoryRepository
                        .findById(
                                request.categoryId()
                        )
                        .orElseThrow(() -> {

                            logger.info(
                                    "Category not found: {}",
                                    request.categoryId()
                            );

                            return new
                                    BillCategoryNotFoundException(
                                            request.categoryId()
                                    );
                        });

        logger.info(
                "Category found: {}",
                category.getCategoryName()
        );

        Bill bill =
                new Bill();

        bill.setUserId(
                request.userId()
        );

        bill.setCategory(
                category
        );

        bill.setDueDate(
                request.dueDate()
        );

        bill.setAmount(
                request.amount()
        );

        bill.setRecurringFrequency(
                request.recurringFrequency()
        );

        bill.setRecurring(
                request.recurring() != null
                        ? request.recurring()
                        : false
        );

        bill.setPaid(
                request.paid() != null
                        ? request.paid()
                        : false
        );

        logger.info(
                "Saving new bill"
        );

        Bill savedBill =
                billRepository.save(bill);

        logger.info(
                "Bill saved successfully with ID: {}",
                savedBill.getBillId()
        );

        return convertToResponse(
                savedBill
        );
    }

    // =====================================================
    // GET ALL BILLS
    // =====================================================

    @Override
    public List<BillResponse>
    getAllBills() {

        logger.info(
                "Fetching all bills"
        );

        List<Bill> bills =
                billRepository.findAll();

        logger.info(
                "Total bills found: {}",
                bills.size()
        );

        return bills.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // GET BILL BY ID
    // =====================================================

    @Override
    public BillResponse getBillById(
            Long billId) {

        logger.info(
                "Fetching bill ID: {}",
                billId
        );

        Bill bill =
                billRepository
                        .findById(billId)
                        .orElseThrow(() -> {

                            logger.info(
                                    "Bill not found: {}",
                                    billId
                            );

                            return new
                                    BillNotFoundException(
                                            billId
                                    );
                        });

        logger.info(
                "Bill found: {}",
                billId
        );

        return convertToResponse(bill);
    }

    // =====================================================
    // UPDATE BILL
    // =====================================================

    @Override
    public BillResponse updateBill(
            Long billId,
            BillRequest request) {

        logger.info(
                "Updating bill ID: {}",
                billId
        );

        Bill bill =
                billRepository
                        .findById(billId)
                        .orElseThrow(() -> {

                            logger.info(
                                    "Bill not found: {}",
                                    billId
                            );

                            return new
                                    BillNotFoundException(
                                            billId
                                    );
                        });

        logger.info(
                "Existing bill found: {}",
                billId
        );

        BillCategory category =
                billCategoryRepository
                        .findById(
                                request.categoryId()
                        )
                        .orElseThrow(() -> {

                            logger.info(
                                    "Category not found: {}",
                                    request.categoryId()
                            );

                            return new
                                    BillCategoryNotFoundException(
                                            request.categoryId()
                                    );
                        });

        bill.setUserId(
                request.userId()
        );

        bill.setCategory(
                category
        );

        bill.setDueDate(
                request.dueDate()
        );

        bill.setAmount(
                request.amount()
        );

        bill.setRecurringFrequency(
                request.recurringFrequency()
        );

        bill.setRecurring(
                request.recurring() != null
                        ? request.recurring()
                        : false
        );

        bill.setPaid(
                request.paid() != null
                        ? request.paid()
                        : false
        );

        logger.info(
                "Saving updated bill: {}",
                billId
        );

        Bill updatedBill =
                billRepository.save(bill);

        logger.info(
                "Bill updated successfully: {}",
                billId
        );

        return convertToResponse(
                updatedBill
        );
    }

    // =====================================================
    // DELETE BILL
    // =====================================================

    @Override
    public void deleteBill(
            Long billId) {

        logger.info(
                "Deleting bill ID: {}",
                billId
        );

        Bill bill =
                billRepository
                        .findById(billId)
                        .orElseThrow(() -> {

                            logger.info(
                                    "Bill not found for deletion: {}",
                                    billId
                            );

                            return new
                                    BillNotFoundException(
                                            billId
                                    );
                        });

        billRepository.delete(bill);

        logger.info(
                "Bill deleted successfully: {}",
                billId
        );
    }

    // =====================================================
    // MARK BILL AS PAID
    // =====================================================

    @Override
    public BillResponse markBillAsPaid(
            Long billId) {

        logger.info(
                "Marking bill as PAID: {}",
                billId
        );

        Bill bill =
                billRepository
                        .findById(billId)
                        .orElseThrow(() -> {

                            logger.info(
                                    "Bill not found: {}",
                                    billId
                            );

                            return new
                                    BillNotFoundException(
                                            billId
                                    );
                        });

        bill.setPaid(true);

        Bill updatedBill =
                billRepository.save(bill);

        logger.info(
                "Bill marked as PAID: {}",
                billId
        );

        return convertToResponse(
                updatedBill
        );
    }

    // =====================================================
    // MARK BILL AS UNPAID
    // =====================================================

    @Override
    public BillResponse markBillAsUnpaid(
            Long billId) {

        logger.info(
                "Marking bill as UNPAID: {}",
                billId
        );

        Bill bill =
                billRepository
                        .findById(billId)
                        .orElseThrow(() -> {

                            logger.info(
                                    "Bill not found: {}",
                                    billId
                            );

                            return new
                                    BillNotFoundException(
                                            billId
                                    );
                        });

        bill.setPaid(false);

        Bill updatedBill =
                billRepository.save(bill);

        logger.info(
                "Bill marked as UNPAID: {}",
                billId
        );

        return convertToResponse(
                updatedBill
        );
    }

    // =====================================================
    // GET BILLS BY USER
    // =====================================================

    @Override
    public List<BillResponse>
    getBillsByUser(
            String userId) {

        logger.info(
                "Fetching bills for user: {}",
                userId
        );

        List<Bill> bills =
                billRepository.findByUserId(
                        userId
                );

        logger.info(
                "Bills found for user {}: {}",
                userId,
                bills.size()
        );

        return bills.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // GET USER + CATEGORY
    // =====================================================

    @Override
    public List<BillResponse>
    getBillsByUserAndCategory(
            String userId,
            Long categoryId) {

        logger.info(
                "Fetching bills for user {} and category {}",
                userId,
                categoryId
        );

        List<Bill> bills =
                billRepository
                        .findByUserIdAndCategory_CategoryId(
                                userId,
                                categoryId
                        );

        logger.info(
                "Bills found: {}",
                bills.size()
        );

        return bills.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // GET USER + STATUS
    // =====================================================

    @Override
    public List<BillResponse>
    getBillsByUserAndStatus(
            String userId,
            Boolean paid) {

        logger.info(
                "Fetching bills for user {} with paid status {}",
                userId,
                paid
        );

        List<Bill> bills =
                billRepository
                        .findByUserIdAndPaid(
                                userId,
                                paid
                        );

        logger.info(
                "Bills found: {}",
                bills.size()
        );

        return bills.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // GET USER + CATEGORY + STATUS
    // =====================================================

    @Override
    public List<BillResponse>
    getBillsByUserCategoryAndStatus(
            String userId,
            Long categoryId,
            Boolean paid) {

        logger.info(
                "Fetching bills for user {}, category {}, paid {}",
                userId,
                categoryId,
                paid
        );

        List<Bill> bills =
                billRepository
                        .findByUserIdAndCategory_CategoryIdAndPaid(
                                userId,
                                categoryId,
                                paid
                        );

        logger.info(
                "Bills found: {}",
                bills.size()
        );

        return bills.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // ENTITY TO RESPONSE
    // =====================================================

    private BillResponse convertToResponse(
            Bill bill) {

        logger.info(
                "Converting bill {} to response",
                bill.getBillId()
        );

        return new BillResponse(

                bill.getBillId(),

                bill.getUserId(),

                bill.getCategory()
                        .getCategoryId(),

                bill.getCategory()
                        .getCategoryName(),

                bill.getDueDate(),

                bill.getAmount(),

                bill.getRecurringFrequency(),

                bill.getRecurring(),

                bill.getPaid()
        );
    }
}
