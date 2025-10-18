package com.example.bikestore.bikestore.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bikestore.bikestore.Models.Transaction;
import com.example.bikestore.bikestore.Repositories.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByCustomerId(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }
}