package com.branas.clean_architecture.application.ports;

import com.branas.clean_architecture.domain.entity.Account;

public interface AccountRepository {

    void emailNotRegistered(String email);
    Account getAccountById(String accountId);
    Account getAccountByEmail(String email);
    Account saveAccount(Account account);

}
