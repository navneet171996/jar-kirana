package com.jar.kirana.services;

import com.jar.kirana.dto.ReportGetDTO;
import com.jar.kirana.dto.TransactionAddDTO;

import java.util.List;

public interface UserService {
    public String recordTransaction(TransactionAddDTO transactionAddDTO);
    public ReportGetDTO getDailyReport(String userId);
    public ReportGetDTO getWeeklyReport(String userId);
    public ReportGetDTO getMonthlyReport(String userId);
    public ReportGetDTO getYearlyReport(String userId);
}
