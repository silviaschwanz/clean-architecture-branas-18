package com.branas.clean_architecture.mocks.samples;

import com.branas.clean_architecture.application.ports.AccountRepository;
import com.branas.clean_architecture.application.GetAccount;
import com.branas.clean_architecture.application.Signup;
import com.branas.clean_architecture.domain.Account;
import com.branas.clean_architecture.driven.adapters.AccountRepositoryMemory;
import com.branas.clean_architecture.driven.adapters.MailerGatewayMemory;
import com.branas.clean_architecture.driver.SignupInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SignupWithMockTest {

    MailerGatewayMemory mailerMock;
    AccountRepository accountDAO;
    Signup signup;
    GetAccount getAccount;

    @Test
    @DisplayName("Deve criar a conta de um passageiro com stub")
    void executeSignupWithStub(){
        mailerMock = Mockito.mock(MailerGatewayMemory.class);
        accountDAO = new AccountRepositoryMemory();
        signup = new Signup(accountDAO, mailerMock);

        String email = "john.doe" + Math.random() + "@gmail.com";
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                email,
                "97456321558",
                "2568-236",
                true,
                false,
                "123"
        );
        doAnswer(invocation -> {
            System.out.println("abc");
            return null;
        }).when(mailerMock).send(eq(email), eq("Welcome!"), eq("..."));

        Account account = signup.execute(signupRequestInput);
        assertNotNull(account.getAccountId());
        verify(mailerMock, times(1)).send(eq(email), eq("Welcome!"), eq("..."));
    }

}