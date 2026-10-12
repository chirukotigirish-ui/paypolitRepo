package dto;

import java.time.LocalDate;

public record BillResponse(

        Long billId,

        String userId,

        Long categoryId,

        String categoryName,

        LocalDate dueDate,

        Double amount,

        String recurringFrequency,

        Boolean recurring,

        Boolean paid

) {
}