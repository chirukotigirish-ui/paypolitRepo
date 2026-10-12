package com.paypilot.service;

import dto.BillRequest;
import dto.BillResponse;

import java.util.List;

public interface BillService {

    BillResponse addBill(
            BillRequest request
    );

    List<BillResponse>
    getAllBills();

    BillResponse getBillById(
            Long billId
    );

    BillResponse updateBill(
            Long billId,
            BillRequest request
    );

    void deleteBill(
            Long billId
    );

    BillResponse markBillAsPaid(
            Long billId
    );

    BillResponse markBillAsUnpaid(
            Long billId
    );

    List<BillResponse>
    getBillsByUser(
            String userId
    );

    List<BillResponse>
    getBillsByUserAndCategory(
            String userId,
            Long categoryId
    );

    List<BillResponse>
    getBillsByUserAndStatus(
            String userId,
            Boolean paid
    );

    List<BillResponse>
    getBillsByUserCategoryAndStatus(
            String userId,
            Long categoryId,
            Boolean paid
    );
}
