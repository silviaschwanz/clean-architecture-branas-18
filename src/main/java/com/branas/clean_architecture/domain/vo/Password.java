package com.branas.clean_architecture.domain.vo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Entity Clean Arch
// Value Object DDD
public class Password {

    private String value;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Password(String value) {
        if(value.length() < 8) throw new RuntimeException("Invalid Password");
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
