package com.minibank.JWT.Mini.Bank.API.dto;

import com.minibank.JWT.Mini.Bank.API.model.TransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String description;
    private LocalDateTime transactionDate;

    public static TransactionDto fromEntity(TransactionEntity transactionEntity){
        return new TransactionDto(
                transactionEntity.getId(),
                transactionEntity.getAmount(),
                transactionEntity.getType().toString(), //converts Enum to String for the UI
                transactionEntity.getDescription(),
                transactionEntity.getTransactionDate()
        );
    }
}
