package com.branas.clean_architecture;

public class ValidateCpf {

    private static final int FACTOR_FIRST_DIGIT = 10;
    private static final int FACTOR_SECOND_DIGIT = 11;
    private static final String NON_NUMERIC_CHARACTER = "\\D";

    public boolean validate(String rawCpf) {
        if (rawCpf == null) return false;
        if(rawCpf.isEmpty()) return false;
        String cpf = clean(rawCpf);
        if (isInvalidLength(cpf)) return false;
        if (allDigitsEqual(cpf)) return false;
        int firstDigit = calculateDigit(cpf, FACTOR_FIRST_DIGIT);
        int secondDigit = calculateDigit(cpf, FACTOR_SECOND_DIGIT);
        return extractDigits(cpf).equals(String.valueOf(firstDigit) + secondDigit);
    }

    private static String clean(String text) {
        return text.replaceAll(NON_NUMERIC_CHARACTER, "");
    }

    private static boolean isInvalidLength(String cpf) {
        return cpf.length() != 11;
    }

    private static boolean allDigitsEqual(String cpf) {
        char firstDigit = cpf.charAt(0);
        return cpf.chars().allMatch(digit -> digit == firstDigit);
    }

    private static int calculateDigit(String cpf, int factor) {
        int total = 0;

        for (int i = 0; i < cpf.length() - 1; i++) {
            if(factor > 1){
                total += Character.getNumericValue(cpf.charAt(i)) * factor--;
            }
        }
        int remainder = total % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }

    private static String extractDigits(String cpf) {
        return cpf.substring(9);
    }
}
