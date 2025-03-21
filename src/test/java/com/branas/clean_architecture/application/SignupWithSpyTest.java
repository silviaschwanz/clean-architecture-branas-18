package com.branas.clean_architecture.application;

import com.branas.clean_architecture.driven.AccountDAOMemory;
import com.branas.clean_architecture.driven.MailerGatewayMemory;
import com.branas.clean_architecture.driver.SignupRequestInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.mockito.Mockito.*;

class SignupWithSpyTest {

    @Spy
    MailerGateway mailerGateway;

    Signup signup;
    GetAccount getAccount;

    @BeforeEach
    public void setUp() {
        mailerGateway = Mockito.spy(new MailerGatewayMemory());
        var accountDAO = new AccountDAOMemory();
        getAccount = new GetAccount(accountDAO);
        signup = new Signup(accountDAO, mailerGateway, getAccount);
    }

    @Test
    @DisplayName("Deve criar a conta de um passageiro com spy")
    void executeSignupWithStub(){
        String email = "john.doe" + Math.random() + "@gmail.com";
        var signupRequestInput = new SignupRequestInput(
                "Joao Paulo",
                email,
                "97456321558",
                "2568-236",
                true,
                false,
                "123"
        );
        signup.execute(signupRequestInput);
        verify(mailerGateway, times(1)).send(anyString(), anyString(), anyString());
        verify(mailerGateway).send(eq(email), eq("Welcome!"), eq("..."));
    }

}