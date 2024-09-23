package com.jar.kirana.services.impl;

import com.jar.kirana.dto.CurrencyConverterResponseDTO;
import com.jar.kirana.dto.ReportGetDTO;
import com.jar.kirana.dto.TransactionAddDTO;
import com.jar.kirana.dto.TransactionGetDTO;
import com.jar.kirana.entities.Transaction;
import com.jar.kirana.entities.TransactionType;
import com.jar.kirana.repositories.TransactionRepository;
import com.jar.kirana.services.CurrencyApiService;
import com.jar.kirana.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final CurrencyApiService currencyApiService;

    public UserServiceImpl(TransactionRepository transactionRepository, CurrencyApiService currencyApiService) {
        this.transactionRepository = transactionRepository;
        this.currencyApiService = currencyApiService;
    }

    @Override
    public String recordTransaction(TransactionAddDTO transactionAddDTO) {
        Transaction transaction = new Transaction(transactionAddDTO);
        transaction.setTransactionDate(LocalDateTime.now());
        CurrencyConverterResponseDTO recentCurrencyConversion = currencyApiService.getRecentCurrencyConversion();
        try{
            Double baseConversionRate = recentCurrencyConversion.getRates().get(String.valueOf(transactionAddDTO.getCurrency()));
            Double inrConversionRate = recentCurrencyConversion.getRates().get("INR");
            transaction.setConversionRate(inrConversionRate/baseConversionRate);
            transaction.setAmountInInr(transaction.getAmount() * transaction.getConversionRate());
        } catch (Exception e) {
            logger.error("Currency {} is not available in currency api", transactionAddDTO.getCurrency());
            throw new RuntimeException("Currency not available");
        }
        Transaction savedTransaction = transactionRepository.save(transaction);
        return savedTransaction.getId();
    }

    @Override
    public ReportGetDTO getDailyReport(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        if(!transactions.isEmpty()){
            transactions = transactions.stream().filter(tr -> tr.getTransactionDate().toLocalDate().isEqual(LocalDate.now())).toList();
            ReportGetDTO reportGetDTO = mapTransactionsToReport(transactions);
            reportGetDTO.setUserId(userId);
            return reportGetDTO;
        }
        ReportGetDTO reportGetDTO = new ReportGetDTO();
        reportGetDTO.setUserId(userId);
        return reportGetDTO;
    }

    @Override
    public ReportGetDTO getWeeklyReport(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        if(!transactions.isEmpty()){
            LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
            LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
            transactions = transactions.stream().filter(x -> {
                LocalDate transactionDate = x.getTransactionDate().toLocalDate();
                return !transactionDate.isBefore(startOfWeek) && !transactionDate.isAfter(endOfWeek);
            }).toList();
            ReportGetDTO reportGetDTO = mapTransactionsToReport(transactions);
            reportGetDTO.setUserId(userId);
            return reportGetDTO;
        }
        ReportGetDTO reportGetDTO = new ReportGetDTO();
        reportGetDTO.setUserId(userId);
        return reportGetDTO;
    }

    @Override
    public ReportGetDTO getMonthlyReport(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        if(!transactions.isEmpty()){
            YearMonth currentMonth = YearMonth.now();
            transactions = transactions.stream()
                    .filter(x -> {
                        LocalDate transactionDate = x.getTransactionDate().toLocalDate();
                        YearMonth transactionMonth = YearMonth.from(transactionDate);
                        return transactionMonth.equals(currentMonth);
                    }).toList();
            ReportGetDTO reportGetDTO = mapTransactionsToReport(transactions);
            reportGetDTO.setUserId(userId);
            return reportGetDTO;
        }
        ReportGetDTO reportGetDTO = new ReportGetDTO();
        reportGetDTO.setUserId(userId);
        return reportGetDTO;
    }

    @Override
    public ReportGetDTO getYearlyReport(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        if(!transactions.isEmpty()){
            int currentYear = LocalDate.now().getYear();
            transactions = transactions.stream()
                    .filter(x -> x.getTransactionDate().getYear() == currentYear)
                    .toList();
            ReportGetDTO reportGetDTO = mapTransactionsToReport(transactions);
            reportGetDTO.setUserId(userId);
            return reportGetDTO;
        }
        ReportGetDTO reportGetDTO = new ReportGetDTO();
        reportGetDTO.setUserId(userId);
        return reportGetDTO;
    }

    private ReportGetDTO mapTransactionsToReport(List<Transaction> transactions){
        ReportGetDTO reportGetDTO = new ReportGetDTO();
        for(Transaction transaction:transactions){
            if(transaction.getTransactionType() == TransactionType.CREDIT){
                reportGetDTO.setTotalCredit(reportGetDTO.getTotalCredit() + transaction.getAmountInInr());
                TransactionGetDTO transactionGetDTO = new TransactionGetDTO(transaction);
                reportGetDTO.getAllCreditTransactions().add(transactionGetDTO);
            }else if(transaction.getTransactionType() == TransactionType.DEBIT){
                reportGetDTO.setTotalDebit(reportGetDTO.getTotalDebit() + transaction.getAmountInInr());
                TransactionGetDTO transactionGetDTO = new TransactionGetDTO(transaction);
                reportGetDTO.getAllDebitTransactions().add(transactionGetDTO);
            }
        }
        reportGetDTO.setBalance(reportGetDTO.getTotalDebit() - reportGetDTO.getTotalCredit());
        return reportGetDTO;
    }
}
