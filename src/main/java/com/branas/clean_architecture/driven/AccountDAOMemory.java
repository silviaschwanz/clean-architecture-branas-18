package com.branas.clean_architecture.driven;

import com.branas.clean_architecture.application.AccountDAO;
import com.branas.clean_architecture.driver.AccountResponse;
import com.branas.clean_architecture.driver.SignupRequestInput;
import com.branas.clean_architecture.driver.SignupResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Profile("memory")
public class AccountDAOMemory implements AccountDAO {

    private List<Account> accounts;

    public AccountDAOMemory() {
        this.accounts = new ArrayList<>();
    }

    public void accountAlreadyExists(String email) {
        boolean exists = accounts.stream().anyMatch(a -> a.email().equals(email));
        if(exists) {
            throw new IllegalStateException("Já existe uma conta com o email informado");
        }
    }

    @Override
    public AccountResponse getAccountById(UUID accountId) {
        Account account = accounts.stream().filter(a -> a.accountId().equals(accountId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Account não encontrada com o accountId informado"
                        ));
        return new AccountResponse(
                account.accountId(),
                account.email(),
                account.name()
        );
    }

    @Override
    public AccountResponse getAccountByEmail(String email) {
        Account account = accounts.stream().filter(a -> a.email().equals(email))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Account não encontrada com o email informado"
                        ));
        return new AccountResponse(
                account.accountId(),
                account.email(),
                account.name()
        );
    }

    @Override
    public SignupResponse saveAccount(SignupRequestInput signupRequestInput) {

        Account account = new Account(
                UUID.randomUUID(),
                signupRequestInput.name(),
                signupRequestInput.email(),
                signupRequestInput.cpf(),
                signupRequestInput.carPlate(),
                signupRequestInput.isPassenger(),
                signupRequestInput.isDriver(),
                signupRequestInput.password(),
                PasswordService.encodePassword(signupRequestInput.password())
        );
        accounts.add(account);
        return new SignupResponse(
                account.accountId()
        );
    }

}
