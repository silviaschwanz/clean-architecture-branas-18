package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.MailerGateway;
import com.branas.clean_architecture.domain.account.Account;
import com.branas.clean_architecture.infra.controller.SignupInput;
import com.branas.clean_architecture.infra.controller.SignupOutput;
import org.springframework.stereotype.Service;

@Service
public class Signup {

    private final AccountRepository accountRepository;

    private final MailerGateway mailerGateway;

    // Dependency Inversion Principle
    // Também é Dendency Injection nessa situação
    public Signup(AccountRepository accountDAO, MailerGateway mailerGateway) {
        this.accountRepository = accountDAO;
        this.mailerGateway = mailerGateway;
    }

    // Use Cases orquestram entidades e recursos
    public SignupOutput execute(SignupInput input) {
        accountRepository.emailNotRegistered(input.email());
        Account account = accountRepository.saveAccount(
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
        return new SignupOutput(
                account.getAccountId()
        );
    }

}


