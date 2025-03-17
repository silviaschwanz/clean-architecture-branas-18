package com.branas.clean_architecture.driver;

import java.util.UUID;

public class SignupResponse {

    public UUID accountId;

    public SignupResponse(UUID accountId) {
        this.accountId = accountId;
    }

}
