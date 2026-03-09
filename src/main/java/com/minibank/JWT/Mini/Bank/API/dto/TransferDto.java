package com.minibank.JWT.Mini.Bank.API.dto;


import jakarta.validation.constraints.*;
import lombok.Data;




import java.math.BigDecimal;

@Data
public class TransferDto {
    // Source account (set automatically by backend)
    private String sourceAccountNumber;

    @NotBlank(message = "Destination account number is required")
    private String destinationAccountNumber;

    @NotBlank(message = "Recipient name is required")
    @Size(min = 2, max = 100, message = "Recipient name must be between 2 and 100 characters")
    private String recipientName;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    @DecimalMin(value = "1.00", message = "Minimum transfer amount is 1")
    private BigDecimal amount;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;


    // Response fields

    private String status;
    private String message;
    private BigDecimal sourceNewBalance;
    private BigDecimal destinationNewBalance;

    // Verification flag (optional)
    private boolean verified = false;
}