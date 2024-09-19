package com.jar.kirana.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
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
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
}
