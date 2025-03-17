package com.branas.clean_architecture.resources;

import com.branas.clean_architecture.driver.AccountResponse;
import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;

import java.util.UUID;

public interface AccountDAO {

    AccountResponse getAccountById(UUID accountId);
    AccountResponse getAccountByEmail(String email);
    SignupResponse saveAccount(SignupRequestInput signupRequestInput);

}
