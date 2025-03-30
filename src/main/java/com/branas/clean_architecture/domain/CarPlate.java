package com.branas.clean_architecture.domain;

public class CarPlate {

    private String value;
    private static final String CAR_PLATE_PATTERN = "[A-Z]{3}[0-9]{4}";


    public CarPlate(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if(!value.matches(CAR_PLATE_PATTERN)) throw new IllegalArgumentException("Invalid car plate");
    }

    public String getValue() {
        return value;
    }

}
