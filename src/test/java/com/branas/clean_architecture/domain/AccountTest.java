package com.branas.clean_architecture.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void shouldCreateAccountDriverSuccessfully() {
        Account account = Account.create(
                "John Doe",
                "john.doe@example.com",
                "97456321558",
                "ABC1234",
                true,
                "StrongPassword123",
                "bcrypt"
        );

        assertNotNull(account.getAccountId());
        assertEquals("John Doe", account.getName());
        assertEquals("john.doe@example.com", account.getEmail());
        assertEquals("97456321558", account.getCpf());
        assertEquals("ABC1234", account.getCarPlate());
        assertFalse(account.isPassenger());
        assertTrue(account.isDriver());
        assertEquals("StrongPassword123", account.getPassword());
        assertEquals("bcrypt", account.getPasswordAlgorithm());
    }

    @Test
    void shouldCreateAccountPassengerSuccessfully() {
        Account account = Account.create(
                "John Doe",
                "john.doe@example.com",
                "97456321558",
                null,
                false,
                "StrongPassword123",
                "bcrypt"
        );

        assertNotNull(account.getAccountId());
        assertEquals("John Doe", account.getName());
        assertEquals("john.doe@example.com", account.getEmail());
        assertEquals("97456321558", account.getCpf());
        assertEquals("", account.getCarPlate());
        assertTrue(account.isPassenger());
        assertFalse(account.isDriver());
        assertEquals("StrongPassword123", account.getPassword());
        assertEquals("bcrypt", account.getPasswordAlgorithm());
    }

    @Test
    void shouldRestoreAccountSuccessfully() {
        UUID accountId = UUID.randomUUID();
        Account account = Account.restore(
                accountId.toString(),
                "Jane Doe",
                "jane.doe@example.com",
                "98765432100",
                "XYZ5678",
                true
        );

        assertEquals(accountId.toString(), account.getAccountId());
        assertEquals("Jane Doe", account.getName());
        assertEquals("jane.doe@example.com", account.getEmail());
        assertEquals("98765432100", account.getCpf());
        assertEquals("XYZ5678", account.getCarPlate());
        assertFalse(account.isPassenger());
        assertTrue(account.isDriver());
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(RuntimeException.class, () -> Account.create(
                "John Doe",
                "invalid-email",
                "12345678901",
                "ABC1234",
                true,
                "StrongPassword123",
                "bcrypt"
        ));
    }

    @Test
    void shouldThrowExceptionForInvalidCpf() {
        assertThrows(IllegalArgumentException.class, () -> Account.create(
                "John Doe",
                "john.doe@example.com",
                "invalid-cpf",
                "ABC1234",
                true,
                "StrongPassword123",
                "bcrypt"
        ));
    }

    @Test
    void shouldThrowExceptionForInvalidCarPlate() {
        assertThrows(IllegalArgumentException.class, () -> Account.create(
                "John Doe",
                "john.doe@example.com",
                "12345678901",
                "INVALID",
                true,
                "StrongPassword123",
                "bcrypt"
        ));
    }
}
