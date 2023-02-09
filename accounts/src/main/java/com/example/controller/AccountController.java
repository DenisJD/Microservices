package com.example.controller;

import com.example.model.Account;
import com.example.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.controller.AccountController.ACCOUNTS_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + ACCOUNTS_CONTROLLER_PATH)
public class AccountController {

    public static final String ACCOUNTS_CONTROLLER_PATH = "/accounts";

    private final AccountRepository accountRepository;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findByOrderById();
    }

}
