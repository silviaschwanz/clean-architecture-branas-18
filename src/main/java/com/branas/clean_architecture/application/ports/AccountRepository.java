package com.branas.clean_architecture.application.ports;

import com.branas.clean_architecture.domain.Account;
import com.branas.clean_architecture.driver.SignupInput;

public interface AccountRepository {

    void accountAlreadyExists(String email);
    Account getAccountById(String accountId);
    Account getAccountByEmail(String email);
    Account saveAccount(SignupInput signupRequestInput);

}
