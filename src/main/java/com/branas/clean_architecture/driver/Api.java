package com.branas.clean_architecture.driver;

import com.branas.clean_architecture.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.UUID;

@RestController
@RequestMapping("/signup")
public class Api {

    @Autowired
    DataSource dataSource;

    @Autowired
    Signup signup;

    @PostMapping()
    public ResponseEntity<?> signup(@RequestBody SignupRequestInput signupRequestInput) {
        try {
            SignupResponse response = signup.execute(signupRequestInput);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseError(e.getMessage()));
        }

    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable UUID accountId) {
        try {
            AccountResponse response = signup.getAccount(accountId);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(e.getMessage()));
        }

    }

}


