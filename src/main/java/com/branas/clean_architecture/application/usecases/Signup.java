package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.MailerGateway;
import com.branas.clean_architecture.domain.account.Account;
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

    // Use Cases orquestram entidades e recursos
    public Account execute(SignupInput input) {
        accountDAO.accountAlreadyExists(input.email());
        Account account = accountDAO.saveAccount(
                Account.create(
                        input.name(),
                        input.email(),
                        input.cpf(),
                        input.carPlate(),
                        input.isDriver(),
                        input.password()
                )
        );
        mailerGateway.send(input.email(), "Welcome!", "...");
        return account;
    }

}


