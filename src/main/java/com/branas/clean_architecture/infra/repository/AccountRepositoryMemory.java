package com.branas.clean_architecture.infra.repository;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.domain.account.Account;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountRepositoryMemory implements AccountRepository {

    private List<Account> accounts;

    public AccountRepositoryMemory() {
        this.accounts = new ArrayList<>();
    }

    public void emailNotRegistered(String email) {
        boolean exists = accounts.stream().anyMatch(a -> a.getEmail().equals(email));
        if(exists) {
            throw new IllegalStateException("There is already an account with that email");
        }
    }

    @Override
    public Account getAccountById(String accountId) {
        return accounts.stream().filter(a -> a.getAccountId().equals(accountId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Account not found")
                );
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accounts.stream().filter(a -> a.getEmail().equals(email))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Account not found")
                );
    }

    @Override
    public Account saveAccount(Account account) {
        accounts.add(account);
        return account;
    }

}
