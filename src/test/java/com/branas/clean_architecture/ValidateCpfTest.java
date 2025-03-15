package com.branas.clean_architecture;

import com.branas.clean_architecture.application.ValidateCpf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateCpfTest {

    @Test
    @DisplayName("Deve validar um cpf com o digito diferente de zero")
    void deveValidarCpfComDigitoDiferenteDeZero() {
        String cpf = "97456321558";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Deve validar um cpf com o primeiro digito zero")
    void deveValidarCpfComPrimeiroDigitoZero() {
        String cpf = "87748248800";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Deve validar um cpf com o segundo digito zero")
    void deveValidarCpfComSegundoDigitoZero() {
        String cpf = "71428793860";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Não deve validar um cpf com menos de onze caracteres")
    void naoDeveValidarCpfComMenosDeOnzeCaracteres() {
        String cpf = "9745632155";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Não deve validar um cpf com os caracteres iguais")
    void naoDeveValidarCpfComTodosOsCaracteresIguais() {
        String cpf = "11111111111";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Não deve validar um cpf que contenha letras")
    void naoDeveValidarCpfComLetras() {
        String cpf = "97a56321558";
        boolean isValid = new ValidateCpf().validate(cpf);
        assertFalse(isValid);
    }

}
