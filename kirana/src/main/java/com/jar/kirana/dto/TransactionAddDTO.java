package com.jar.kirana.dto;

import com.jar.kirana.entities.Currency;
import com.jar.kirana.entities.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionAddDTO {
    private String userId;
    private Double amount;
    private Currency currency;
    private TransactionType transactionType;
}
