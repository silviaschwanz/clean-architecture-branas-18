package com.branas.clean_architecture.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarPlateTest {

    @Test
    void shouldCreateCarPlateWhenValid() {
        CarPlate carPlate = new CarPlate("ABC1234");
        assertEquals("ABC1234", carPlate.getValue());
    }

    @Test
    void shouldThrowExceptionWhenCarPlateIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new CarPlate("1234ABC"));
        assertThrows(IllegalArgumentException.class, () -> new CarPlate("AB12345"));
        assertThrows(IllegalArgumentException.class, () -> new CarPlate("ABCDE12"));
        assertThrows(IllegalArgumentException.class, () -> new CarPlate("A1B2C3D"));
    }

    @Test
    void shouldThrowExceptionWhenCarPlateContainsLowerCaseLetters() {
        assertThrows(IllegalArgumentException.class, () -> new CarPlate("abc1234"));
    }

    @Test
    void shouldThrowExceptionWhenCarPlateIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new CarPlate(""));
    }

    @Test
    void shouldThrowExceptionWhenCarPlateIsNull() {
        assertThrows(NullPointerException.class, () -> new CarPlate(null));
    }

}