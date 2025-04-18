package com.branas.clean_architecture.mocks.samples;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.ports.MailerGateway;
import com.branas.clean_architecture.application.usecases.Signup;
import com.branas.clean_architecture.domain.account.Account;
import com.branas.clean_architecture.infra.controller.SignupInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignupWithStubTest {

    @Mock
    MailerGateway mailerGateway;

    @Mock
    AccountRepository accountRepository;


    @Test
    @DisplayName("Deve criar a conta de um passageiro com stub")
    void executeSignupWithStub(){
        Signup signup = new Signup(accountRepository, mailerGateway);
        doNothing().when(accountRepository).emailNotRegistered(anyString());
        Account savedAccount = Account.restore(
                UUID.randomUUID().toString(),
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "ABC1234",
                true
        );
        doNothing().when(mailerGateway).send(anyString(), anyString(), anyString());
        when(accountRepository.saveAccount(any(Account.class))).thenReturn(savedAccount);
        var signupRequestInput = new SignupInput(
                savedAccount.getName(),
                savedAccount.getEmail(),
                savedAccount.getCpf(),
                savedAccount.getCarPlate(),
                savedAccount.isPassenger(),
                savedAccount.isDriver(),
                "123"
        );
        signup.execute(signupRequestInput);
        verify(mailerGateway, times(1)).send(anyString(), anyString(), anyString());
        verify(accountRepository, times(1)).saveAccount(any(Account.class));
    }

}