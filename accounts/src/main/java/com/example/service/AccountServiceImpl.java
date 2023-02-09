package com.example.service;

import com.example.model.Account;
import com.example.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public void debitBalance(final String code, final BigDecimal amount) {
        final Account accountToUpdate = accountRepository.findByCode(code).get();
        BigDecimal currentBalance = accountToUpdate.getBalance();
        BigDecimal newBalance = currentBalance.subtract(amount);
        accountToUpdate.setBalance(newBalance);
        accountRepository.save(accountToUpdate);
    }

    @Override
    public void creditBalance(final String code, final BigDecimal amount) {
        final Account accountToUpdate = accountRepository.findByCode(code).get();
        BigDecimal currentBalance = accountToUpdate.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        accountToUpdate.setBalance(newBalance);
        accountRepository.save(accountToUpdate);
    }
}
