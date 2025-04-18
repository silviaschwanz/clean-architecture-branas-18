package com.branas.clean_architecture.domain.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    void shouldCreateNameWhenValid() {
        Name name = new Name("John Doe");
        assertEquals("John Doe", name.getValue());
    }

    @Test
    void shouldThrowExceptionWhenNameContainsNumbers() {
        assertThrows(RuntimeException.class, () -> new Name("John Doe123"));
    }

    @Test
    void shouldThrowExceptionWhenNameContainsSpecialCharacters() {
        assertThrows(RuntimeException.class, () -> new Name("John@Doe"));
        assertThrows(RuntimeException.class, () -> new Name("John_Doe"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(RuntimeException.class, () -> new Name(""));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

}