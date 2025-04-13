package com.branas.clean_architecture.domain.account;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Password {

    private String value;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Password(String value) {
        this.value = encodePassword(value);
    }

    public String getValue() {
        return value;
    }

    private String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

}
