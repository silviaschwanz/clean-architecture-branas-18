package com.branas.clean_architecture.domain;

public class Name {

    private String value;
    private static final String NAME_PATTERN = "^[a-zA-Z]+( [a-zA-Z]+)*$";

    public Name(String value) {
        if(!value.matches(NAME_PATTERN)) throw new IllegalArgumentException("Invalid name. Only letters and spaces are allowed");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
