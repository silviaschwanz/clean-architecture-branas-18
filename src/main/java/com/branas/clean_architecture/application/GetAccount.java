package com.branas.clean_architecture.application;

import com.branas.clean_architecture.driver.AccountResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetAccount {

    private final AccountDAO accountDAO;

    public GetAccount(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public AccountResponse execute(UUID accountId) {
        return accountDAO.getAccountById(accountId);
    }

}
