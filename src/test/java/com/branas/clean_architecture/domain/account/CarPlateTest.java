package com.branas.clean_architecture.domain.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarPlateTest {

    @Test
    void shouldCreateCarPlateWithValidValue() {
        CarPlate plate = new CarPlate("ABC1234");
        assertEquals("ABC1234", plate.getValue());
    }

    @Test
    void shouldThrowExceptionForInvalidFormatLowerCase() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarPlate("abc1234");
        });
        assertEquals("Invalid car plate", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForTooShort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarPlate("AB123");
        });
        assertEquals("Invalid car plate", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidCharacters() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarPlate("A1C12@4");
        });
        assertEquals("Invalid car plate", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyString() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CarPlate("");
        });
        assertEquals("Invalid car plate", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CarPlate(null);
        });
        assertEquals("Cannot invoke \"String.matches(String)\" because \"value\" is null", exception.getMessage());
    }
}