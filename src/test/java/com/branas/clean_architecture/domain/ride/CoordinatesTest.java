package com.branas.clean_architecture.domain.ride;

import com.branas.clean_architecture.domain.vo.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {

    @Test
    void shouldCreateCoordinatesWithValidValues() {
        Coordinates coordinates = new Coordinates(45.0, 90.0);
        assertEquals(45.0, coordinates.latitude());
        assertEquals(90.0, coordinates.longitude());
    }

    @Test
    void shouldThrowErrorWhenLatitudeIsLessThanMinus90() {
        Error error = assertThrows(Error.class, () -> new Coordinates(-91.0, 0.0));
        assertEquals("Invalid latitude", error.getMessage());
    }

    @Test
    void shouldThrowErrorWhenLatitudeIsGreaterThan90() {
        Error error = assertThrows(Error.class, () -> new Coordinates(91.0, 0.0));
        assertEquals("Invalid latitude", error.getMessage());
    }

    @Test
    void shouldThrowErrorWhenLongitudeIsLessThanMinus180() {
        Error error = assertThrows(Error.class, () -> new Coordinates(0.0, -181.0));
        assertEquals("Invalid longitude", error.getMessage());
    }

    @Test
    void shouldThrowErrorWhenLongitudeIsGreaterThan180() {
        Error error = assertThrows(Error.class, () -> new Coordinates(0.0, 181.0));
        assertEquals("Invalid longitude", error.getMessage());
    }

    @Test
    void shouldAllowBoundaryValues() {
        assertDoesNotThrow(() -> new Coordinates(-90.0, -180.0));
        assertDoesNotThrow(() -> new Coordinates(90.0, 180.0));
    }

}