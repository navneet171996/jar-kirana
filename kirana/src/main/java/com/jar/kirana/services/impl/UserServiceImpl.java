package com.jar.kirana.services.impl;

import com.jar.kirana.dto.CurrencyConverterResponseDTO;
import com.jar.kirana.dto.TransactionAddDTO;
import com.jar.kirana.entities.Transaction;
import com.jar.kirana.repositories.TransactionRepository;
import com.jar.kirana.services.CurrencyApiService;
import com.jar.kirana.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

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
            throw new RuntimeException("Currency not available");
        }
        Transaction savedTransaction = transactionRepository.save(transaction);
        return savedTransaction.getId();
    }
}
