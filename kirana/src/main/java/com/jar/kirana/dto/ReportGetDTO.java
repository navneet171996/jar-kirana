package com.jar.kirana.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportGetDTO {
    private String userId;
    private Double totalDebit;
    private Double totalCredit;
    private Double balance;
    private List<TransactionGetDTO> allDebitTransactions;
    private List<TransactionGetDTO> allCreditTransactions;

    public ReportGetDTO(){
        this.totalCredit=0.0;
        this.totalDebit=0.0;
        this.balance = 0.0;
        this.allCreditTransactions = new ArrayList<>();
        this.allDebitTransactions = new ArrayList<>();
    }
}
