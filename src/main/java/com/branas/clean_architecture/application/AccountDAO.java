package com.branas.clean_architecture.application;

import com.branas.clean_architecture.driver.AccountResponse;
import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;

import java.util.UUID;

public interface AccountDAO {

    void accountAlreadyExists(String email);
    AccountResponse getAccountById(UUID accountId);
    AccountResponse getAccountByEmail(String email);
    SignupResponse saveAccount(SignupRequestInput signupRequestInput);

}
