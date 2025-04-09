package com.branas.clean_architecture.driver;

import com.branas.clean_architecture.application.usecases.GetAccount;
import com.branas.clean_architecture.application.RequestRide;
import com.branas.clean_architecture.application.usecases.Signup;
import com.branas.clean_architecture.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class Api {

    @Autowired
    Signup signup;

    @Autowired
    GetAccount getAccount;

    @Autowired
    RequestRide requestRide;


    @PostMapping()
    public ResponseEntity<?> signup(@RequestBody SignupInput signupInput) {
        try {
            Account account = signup.execute(signupInput);
            SignupOutput response = new SignupOutput(account.getAccountId());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorOutput(e.getMessage()));
        }

    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable String accountId) {
        try {
            var accountOutput = getAccount.execute(accountId);
            return ResponseEntity.ok().body(accountOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorOutput(e.getMessage()));
        }

    }

    public ResponseEntity<?> getRide(@RequestBody RideInput input) {
        try {
            var rideOutput = requestRide.execute(input);
            return ResponseEntity.ok().body(rideOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorOutput(e.getMessage()));
        }
    }

}


