package com.branas.clean_architecture.domain.account;

import java.util.stream.IntStream;

public record Cpf(String value) {

    private static final int CPF_VALID_LENGTH = 11;
    private static final int FACTOR_FIRST_DIGIT = 10;
    private static final int FACTOR_SECOND_DIGIT = 11;
    private static final String NON_NUMERIC_CHARACTER = "\\D";


    public Cpf {
        cpfNotNull(value);
        String cleanedCpf = clean(value);
        isValidLength(cleanedCpf);
        allDigitsTheSame(cleanedCpf);
        validateDigits(cleanedCpf);
        value = cleanedCpf;
    }

    private void cpfNotNull(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("CPF is null or is blank");
    }

    private String clean(String value) {
        return value.replaceAll(NON_NUMERIC_CHARACTER, "");
    }

    private void isValidLength(String cpf) {
        if (cpf.length() != CPF_VALID_LENGTH) throw new IllegalArgumentException("CPF is invalid lenght");
    }

    private void allDigitsTheSame(String cpf) {
        if (cpf.chars().distinct().count() == 1) throw new IllegalArgumentException("CPF with all digits equals");
    }

    private void validateDigits(String cpf) {
        String cpfBase = cpf.substring(0, 9);
        String firstDigit = String.valueOf(calculateDigit(cpfBase, FACTOR_FIRST_DIGIT));
        String secondDigit = String.valueOf(calculateDigit(cpfBase + firstDigit, FACTOR_SECOND_DIGIT));
        if (!cpf.endsWith(firstDigit + secondDigit)) {
            throw new IllegalArgumentException("Invalid CPF");
        }
    }

    private int calculateDigit(String cpf, int factor) {
        int sum = IntStream.range(0, cpf.length())
                .map(i -> Character.getNumericValue(cpf.charAt(i)) * (factor - i))
                .sum();
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }

}
