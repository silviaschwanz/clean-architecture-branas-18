package com.branas.clean_architecture.application;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.MailerGateway;
import com.branas.clean_architecture.domain.Account;
import com.branas.clean_architecture.driver.SignupInput;
import org.springframework.stereotype.Service;

@Service
public class Signup {

    private final AccountRepository accountDAO;

    private final MailerGateway mailerGateway;

    // Dependency Inversion Principle
    // Também é Dendency Injection nessa situação
    public Signup(AccountRepository accountDAO, MailerGateway mailerGateway) {
        this.accountDAO = accountDAO;
        this.mailerGateway = mailerGateway;
    }

    public Account execute(SignupInput signupRequestInput) {
        accountDAO.accountAlreadyExists(signupRequestInput.email());
        Account account = accountDAO.saveAccount(signupRequestInput);
        mailerGateway.send(signupRequestInput.email(), "Welcome!", "...");
        return account;
    }

}


