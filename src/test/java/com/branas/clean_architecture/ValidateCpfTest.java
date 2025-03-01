package com.branas.clean_architecture;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateCpfTest {

    @Test
    void deveValidarCpfComDigitoDiferenteDeZero() {
        String cpf = "97456321558";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertTrue(isValid);
    }

    @Test
    void deveValidarCpfComSegundoDigitoZero() {
        String cpf = "71428793860";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertTrue(isValid);
    }

    @Test
    void deveValidarCpfComPrimeiroDigitoZero() {
        String cpf = "87748248800";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertTrue(isValid);
    }

    @Test
    void naoDeveValidarCpfComMenosDeOnzeCaracteres() {
        String cpf = "9745632155";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertFalse(isValid);
    }

    @Test
    void naoDeveValidarCpfComTodosOsCaracteresIguais() {
        String cpf = "11111111111";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertFalse(isValid);
    }

    @Test
    void naoDeveValidarCpfComLetras() {
        String cpf = "97a56321558";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertFalse(isValid);
    }
}
