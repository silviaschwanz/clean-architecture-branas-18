package com.branas.clean_architecture.application;

import com.branas.clean_architecture.driven.Account;
import com.branas.clean_architecture.driver.SignupRequestInput;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Signup {

    private final AccountDAO accountDAO;

    private final MailerGateway mailerGateway;

    private final GetAccount getAccount;

    public Signup(AccountDAO accountDAO, MailerGateway mailerGateway, GetAccount getAccount) {
        this.accountDAO = accountDAO;
        this.mailerGateway = mailerGateway;
        this.getAccount = getAccount;
    }

    public Account execute(SignupRequestInput signupRequestInput) {
        accountDAO.accountAlreadyExists(signupRequestInput.email());
        if(!signupRequestInput.name().matches("[a-zA-Z]+\\s[a-zA-Z]+")) {
            throw new IllegalArgumentException("Nome inválido");
        }
        if(!signupRequestInput.email().matches("^(.+)@(.+)$")) {
            throw new IllegalArgumentException("Email inválido");
        }
        if(!new ValidateCpf().validate(signupRequestInput.cpf())) {
            throw new IllegalArgumentException("Cpf inválido");
        }
        if(signupRequestInput.isDriver() && !signupRequestInput.carPlate().matches("[A-Z]{3}[0-9]{4}")){
            throw new IllegalArgumentException("Placa do carro inválida");
        }
        UUID accountID = accountDAO.saveAccount(signupRequestInput);
        mailerGateway.send(signupRequestInput.email(), "Welcome!", "...");
        return getAccount.execute(accountID);
    }

}


