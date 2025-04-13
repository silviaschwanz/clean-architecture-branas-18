package com.branas.clean_architecture.domain;

import com.branas.clean_architecture.domain.account.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldCreateEmailWhenValid() {
        Email email = new Email("test@example.com");
        assertEquals("test@example.com", email.getValue());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        assertThrows(RuntimeException.class, () -> new Email("invalid-email"));
        assertThrows(RuntimeException.class, () -> new Email("@example.com"));
        assertThrows(RuntimeException.class, () -> new Email("test@.com"));
        assertThrows(RuntimeException.class, () -> new Email("test@example"));
        assertThrows(RuntimeException.class, () -> new Email("test@com."));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        assertThrows(RuntimeException.class, () -> new Email(""));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }
}