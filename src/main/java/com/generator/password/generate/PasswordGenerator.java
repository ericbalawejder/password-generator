package com.generator.password.generate;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Passwords should be stored in char[] but does .toCharArray() leave a copy of the String
 * in memory?
 *
 * Should generateRandomPassword() return char[], List<Character> or String?
 */
public class PasswordGenerator {

    private static final String LOWERCASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHAR = LOWERCASE_CHAR.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String PUNCTUATION = "!@#&()–[{}]:;',?/*";
    private static final String SYMBOL = "~$^+=<>";
    private static final String SPECIAL_CHAR = PUNCTUATION + SYMBOL;

    private static final String PASSWORD_POLICY =
            LOWERCASE_CHAR + UPPERCASE_CHAR + DIGIT + SPECIAL_CHAR;

    private static final SecureRandom secureRandom = new SecureRandom();

    public static void main(String[] args) {
        // Always store a password in char[]?
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        final char[] password = passwordGenerator
                .generateRandomPassword(32, 2, 2, 2, 2)
                .toCharArray();

        System.out.println(password);
    }

    // TODO: Place protections on parameter input size
    public String generateRandomPassword(int length, int lowercase, int upperCase, int digit, int specialChar) {
        if (length < 8) {
            throw new IllegalArgumentException("password length must be greater than 8 characters");
        }
        final String password = generateRandomString(LOWERCASE_CHAR, lowercase) +
                generateRandomString(UPPERCASE_CHAR, upperCase) +
                generateRandomString(DIGIT, digit) +
                generateRandomString(SPECIAL_CHAR, specialChar) +
                generateRandomString(PASSWORD_POLICY, length - lowercase - upperCase - digit - specialChar);

        return shuffleString(password);
    }

    private String generateRandomString(String alphabet, int size) {
        if (alphabet == null || alphabet.length() <= 0) throw new IllegalArgumentException("Invalid alphabet.");
        if (size < 0) throw new IllegalArgumentException("Invalid size.");

        return IntStream.range(0, size)
                .map(i -> secureRandom.nextInt(alphabet.length()))
                .mapToObj(alphabet::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private String shuffleString(String input) {
        final List<String> result = Arrays.asList(input.split(""));
        Collections.shuffle(result);
        return String.join("", result);
    }

}
