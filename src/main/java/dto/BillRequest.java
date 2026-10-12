package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BillRequest(

        @NotBlank
        String userId,

        @NotNull
        Long categoryId,

        @NotNull
        LocalDate dueDate,

        @NotNull
        @Positive
        Double amount,

        String recurringFrequency,

        Boolean recurring,

        Boolean paid

) {
}