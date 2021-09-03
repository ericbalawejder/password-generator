package com.generator.password.entity;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Passwords should be stored in char[] but does .toCharArray() leave a copy of the String
 * in memory?
 * <p>
 * Should generateRandomPassword() return char[], List<Character> or String?
 */
public class PasswordGenerator implements Serializable {

    private static final String LOWERCASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHAR = LOWERCASE_CHAR.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String PUNCTUATION = "!@#&()–[{}]:;',?/*";
    private static final String SYMBOL = "~$^+=<>";
    private static final String SPECIAL_CHAR = PUNCTUATION + SYMBOL;

    private static final String PASSWORD_POLICY =
            LOWERCASE_CHAR + UPPERCASE_CHAR + DIGIT + SPECIAL_CHAR;

    private static final SecureRandom secureRandom = new SecureRandom();

    private int length;
    private int lowercase;
    private int uppercase;
    private int digit;
    private int specialChar;
    private String password;

    public PasswordGenerator() {
    }

    public PasswordGenerator(int length, int lowercase, int uppercase, int digit, int specialChar) {
        this.length = length;
        this.lowercase = lowercase;
        this.uppercase = uppercase;
        this.digit = digit;
        this.specialChar = specialChar;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLowercase() {
        return lowercase;
    }

    public void setLowercase(int lowercase) {
        this.lowercase = lowercase;
    }

    public int getUppercase() {
        return uppercase;
    }

    public void setUppercase(int uppercase) {
        this.uppercase = uppercase;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public int getSpecialChar() {
        return specialChar;
    }

    public void setSpecialChar(int specialChar) {
        this.specialChar = specialChar;
    }

    public String getPassword() {
        return Objects.requireNonNullElseGet(password, () -> generateRandomPassword(
                this.length, this.lowercase, this.uppercase, this.digit, this.specialChar));
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordGenerator{" +
                "length=" + length +
                ", lowercase=" + lowercase +
                ", uppercase=" + uppercase +
                ", digit=" + digit +
                ", specialChar=" + specialChar +
                ", password='" + password + '\'' +
                '}';
    }

    // TODO: Place protections on parameter input size. Use char[].
    private String generateRandomPassword(int length, int lowercase, int uppercase, int digit, int specialChar) {
        if (length < 8 || length > 128) {
            throw new IllegalArgumentException("password length must be 8 - 128 characters");
        }
        final String password = generateRandomString(LOWERCASE_CHAR, lowercase) +
                generateRandomString(UPPERCASE_CHAR, uppercase) +
                generateRandomString(DIGIT, digit) +
                generateRandomString(SPECIAL_CHAR, specialChar) +
                generateRandomString(PASSWORD_POLICY, length - lowercase - uppercase - digit - specialChar);

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
