package com.branas.clean_architecture.mocks.samples;

import com.branas.clean_architecture.application.AccountDAO;
import com.branas.clean_architecture.application.GetAccount;
import com.branas.clean_architecture.application.MailerGateway;
import com.branas.clean_architecture.application.Signup;
import com.branas.clean_architecture.driver.SignupRequestInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class SignupWithStubTest {

    @Mock
    MailerGateway mailerGateway;

    @Mock
    AccountDAO accountDAO;


    @Test
    @DisplayName("Deve criar a conta de um passageiro com stub")
    void executeSignupWithStub(){
        MockitoAnnotations.openMocks(this);
        GetAccount getAccount = new GetAccount(accountDAO);
        Signup signup = new Signup(accountDAO, mailerGateway, getAccount);

        doNothing().when(mailerGateway).send(anyString(), anyString(), anyString());
        when(accountDAO.getAccountByEmail(anyString())).thenReturn(null);
        var signupRequestInput = new SignupRequestInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "2568-236",
                true,
                false,
                "123"
        );
        signup.execute(signupRequestInput);
        verify(mailerGateway, times(1)).send(anyString(), anyString(), anyString());
    }

}