package com.branas.clean_architecture.application.ports;

import com.branas.clean_architecture.domain.account.Account;

public interface AccountRepository {

    void accountAlreadyExists(String email);
    Account getAccountById(String accountId);
    Account getAccountByEmail(String email);
    Account saveAccount(Account account);

}
