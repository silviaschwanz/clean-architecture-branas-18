package com.branas.clean_architecture.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CpfTest {

    @Test
    @DisplayName("Deve validar um cpf com o digito diferente de zero")
    void deveValidarCpfComDigitoDiferenteDeZero() {
        String value = "97456321558";
        Cpf cpf = new Cpf(value);
        assertNotNull(cpf);
    }

    @Test
    @DisplayName("Deve validar um cpf com o primeiro digito zero")
    void deveValidarCpfComPrimeiroDigitoZero() {
        Cpf cpf = new Cpf("87748248800");
        assertNotNull(cpf);
    }

    @Test
    @DisplayName("Deve validar um cpf com o segundo digito zero")
    void deveValidarCpfComSegundoDigitoZero() {
        Cpf cpf = new Cpf("71428793860");
        assertNotNull(cpf);
    }

    @Test
    void shouldNotValidateCPFWithInvalidLenght() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cpf("9745632155");
        });
        assertEquals("CPF is invalid lenght", exception.getMessage());
    }

    @Test
    void shouldNotValidateCPFWithAllDigitsTheSame() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cpf("11111111111");
        });
        assertEquals("CPF with all digits equals", exception.getMessage());
    }

    @Test
    void shouldNotValidateCPFWithLetters() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cpf("97a56321558");
        });
        assertEquals("CPF is invalid lenght", exception.getMessage());
    }

}
