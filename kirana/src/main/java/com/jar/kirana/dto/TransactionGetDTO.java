package com.jar.kirana.dto;

import com.jar.kirana.entities.Currency;
import com.jar.kirana.entities.Transaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionGetDTO {
    private LocalDateTime transactionDate;
    private Double amount;
    private Currency currency;
    private Double amountInInr;

    public TransactionGetDTO(Transaction transaction) {
        this.transactionDate = transaction.getTransactionDate();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.amountInInr = transaction.getAmountInInr();
    }
}
