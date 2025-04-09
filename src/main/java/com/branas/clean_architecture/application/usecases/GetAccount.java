package com.branas.clean_architecture.application.usecases;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.domain.Account;
import com.branas.clean_architecture.driver.AccountOutput;
import org.springframework.stereotype.Service;

@Service
public class GetAccount {

    private final AccountRepository accountRepository;

    public GetAccount(AccountRepository accountDAO) {
        this.accountRepository = accountDAO;
    }

    public AccountOutput execute(String accountId) {
        Account account = accountRepository.getAccountById(accountId);
        return new AccountOutput(
                account.getAccountId(),
                account.getName(),
                account.getEmail(),
                account.getCpf(),
                account.getCarPlate(),
                account.isPassenger(),
                account.isDriver()
        );
    }

}
