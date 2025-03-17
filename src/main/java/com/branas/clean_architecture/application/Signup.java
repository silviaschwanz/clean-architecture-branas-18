package com.branas.clean_architecture.application;

import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;
import com.branas.clean_architecture.resources.AccountDAO;
import org.springframework.stereotype.Service;

@Service
public class Signup {

    private final AccountDAO accountDAO;

    public Signup(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public SignupResponse execute(SignupRequestInput signupRequestInput) {
        if(accountDAO.getAccountByEmail(signupRequestInput.email()) != null) {
            throw new IllegalArgumentException("O email já existe");
        }
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
        return accountDAO.saveAccount(signupRequestInput);
    }

}


