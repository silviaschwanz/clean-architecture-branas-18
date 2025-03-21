package com.branas.clean_architecture.driver;

import com.branas.clean_architecture.application.GetAccount;
import com.branas.clean_architecture.application.Signup;
import com.branas.clean_architecture.driven.Account;
import com.branas.clean_architecture.driven.AccountDAOPostgres;
import com.branas.clean_architecture.driven.MailerGatewayMemory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/signup")
public class Api {

    private final Signup signup;

    private final GetAccount getAccount;

    public Api(AccountDAOPostgres accountDAO, MailerGatewayMemory mailerGateway) {
        this.getAccount = new GetAccount(accountDAO);
        this.signup = new Signup(accountDAO, mailerGateway, getAccount);
    }

    @PostMapping()
    public ResponseEntity<?> signup(@RequestBody SignupRequestInput signupRequestInput) {
        try {
            Account account = signup.execute(signupRequestInput);
            SignupResponse response = new SignupResponse(account.accountId());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseError(e.getMessage()));
        }

    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable UUID accountId) {
        try {
            var account = getAccount.execute(accountId);
            var response =  new AccountResponse(account.accountId(), account.email(), account.name());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(e.getMessage()));
        }

    }

}


