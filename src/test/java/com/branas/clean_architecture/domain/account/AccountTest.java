package com.branas.clean_architecture.domain.account;

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
                "StrongPassword123"
        );
        assertNotNull(account.getAccountId());
        assertEquals("John Doe", account.getName());
        assertEquals("john.doe@example.com", account.getEmail());
        assertEquals("97456321558", account.getCpf());
        assertEquals("ABC1234", account.getCarPlate());
        assertFalse(account.isPassenger());
        assertTrue(account.isDriver());
        assertTrue(account.passwordMatch("StrongPassword123", account.getPassword()));
    }

    @Test
    void shouldCreateAccountPassengerSuccessfully() {
        Account account = Account.create(
                "John Doe",
                "john.doe@example.com",
                "97456321558",
                null,
                false,
                "StrongPassword123"
        );
        assertNotNull(account.getAccountId());
        assertEquals("John Doe", account.getName());
        assertEquals("john.doe@example.com", account.getEmail());
        assertEquals("97456321558", account.getCpf());
        assertEquals("", account.getCarPlate());
        assertTrue(account.isPassenger());
        assertFalse(account.isDriver());
        assertTrue(account.passwordMatch("StrongPassword123", account.getPassword()));
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
    void shouldThrowExceptionForInvalidName(){
        assertThrows(RuntimeException.class, () -> Account.create(
                "joao 123",
                "invalid-email",
                "12345678901",
                "ABC1234",
                true,
                "StrongPassword123"
        ));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(RuntimeException.class, () -> Account.create(
                "John Doe",
                "invalid-email",
                "12345678901",
                "ABC1234",
                true,
                "StrongPassword123"
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
                "StrongPassword123"
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
                "StrongPassword123"
        ));
    }

    @Test
    void shouldThrowExceptionForInvalidLenghtCpf(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "John Doe",
                    "john.doe@example.com",
                    "9745632155",
                    "",
                    true,
                    "StrongPassword123"
            );
        });
        assertEquals("CPF is invalid lenght", exception.getMessage());
    }

    @Test
    void shouldNotSignupInvalidCarPlate(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "John Doe",
                    "john.doe@example.com",
                    "97456321558",
                    "A23",
                    true,
                    "StrongPassword123"
            );
        });
        assertEquals("Invalid car plate", exception.getMessage());
    }

}
