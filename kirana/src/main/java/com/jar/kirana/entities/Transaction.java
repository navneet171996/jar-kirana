package com.jar.kirana.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jar.kirana.dto.TransactionAddDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "transactions")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    @Id
    private String id;
    private String userId;
    private Double amount;
    private Currency currency;
    private Double conversionRate;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;

    public Transaction(TransactionAddDTO transactionAddDTO){
        this.userId = transactionAddDTO.getUserId();
        this.amount = transactionAddDTO.getAmount();
        this.currency = transactionAddDTO.getCurrency();
        this.transactionType = transactionAddDTO.getTransactionType();

    }
}
