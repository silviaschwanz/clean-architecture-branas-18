package com.branas.clean_architecture.mocks.samples;

import com.branas.clean_architecture.application.ports.MailerGateway;
import com.branas.clean_architecture.application.usecases.Signup;
import com.branas.clean_architecture.infra.repository.AccountRepositoryMemory;
import com.branas.clean_architecture.infra.gateway.MailerGatewayMemory;
import com.branas.clean_architecture.application.dto.SignupInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.mockito.Mockito.*;

class SignupWithSpyTest {

    @Spy
    MailerGateway mailerGateway;


    @Test
    @DisplayName("Deve criar a conta de um passageiro com spy")
    void executeSignupWithStub(){
        mailerGateway = Mockito.spy(new MailerGatewayMemory());
        var accountDAO = new AccountRepositoryMemory();
        Signup signup = new Signup(accountDAO, mailerGateway);

        String email = "john.doe" + Math.random() + "@gmail.com";
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                email,
                "97456321558",
                "2568-236",
                true,
                false,
                "12345678"
        );
        signup.execute(signupRequestInput);
        verify(mailerGateway, times(1)).send(anyString(), anyString(), anyString());
        verify(mailerGateway).send(eq(email), eq("Welcome!"), eq("..."));
    }

}