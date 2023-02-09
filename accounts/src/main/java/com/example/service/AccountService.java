package com.example.service;

import java.math.BigDecimal;

public interface AccountService {

    void debitBalance(String code, BigDecimal amount);

    void creditBalance(String code, BigDecimal amount);

}
