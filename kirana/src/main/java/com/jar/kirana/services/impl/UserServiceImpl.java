package com.jar.kirana.services.impl;

import com.jar.kirana.dto.TransactionAddDTO;
import com.jar.kirana.entities.Transaction;
import com.jar.kirana.repositories.TransactionRepository;
import com.jar.kirana.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final TransactionRepository transactionRepository;

    public UserServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public String recordTransaction(TransactionAddDTO transactionAddDTO) {
        Transaction transaction = new Transaction(transactionAddDTO);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setConversionRate(1.0);
        //set the conversion rate according to the API response later

        Transaction savedTransaction = transactionRepository.save(transaction);
        return savedTransaction.getId();
    }
}
