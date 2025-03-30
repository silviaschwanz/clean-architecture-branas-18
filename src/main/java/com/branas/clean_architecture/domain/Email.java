package com.branas.clean_architecture.domain;

public class Email {

    private String value;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public Email(String value) {
        if(!value.matches(EMAIL_PATTERN)) throw new IllegalArgumentException("Invalid email");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
