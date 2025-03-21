package com.branas.clean_architecture.application;

import com.branas.clean_architecture.driven.Account;
import com.branas.clean_architecture.driver.SignupRequestInput;

import java.util.UUID;

public interface AccountDAO {

    void accountAlreadyExists(String email);
    Account getAccountById(UUID accountId);
    Account getAccountByEmail(String email);
    UUID saveAccount(SignupRequestInput signupRequestInput);

}
