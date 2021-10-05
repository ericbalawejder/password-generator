package com.generator.password.entity;

import com.generator.password.exception.PasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordGeneratorTest {
    /**
     * Used positive lookahead (?=X) for every group of characters. We expect X to be found after
     * the beginning of the String (marked with ^) in order to match, but we don't want to go to
     * the end of X, rather we want to stay at the beginning of the line. We didn't use [A-Z] or [a-z]
     * for letter groups, but \p{Lu} and \p{Ll} instead. These will match any kind of letter
     * (in our case, uppercase and lowercase respectively) from any language, not only English.
     */
    private static final String REGEX = "^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)" +
            "(?=.*?[~!@#$^&*()\\-=+\\[{\\]};:',<>/?]).*$";

    @Test
    void getPasswordContainsAtLeastOneUppercaseDigitSpecialChar() {
        final PasswordGenerator generator = new PasswordGenerator(32, 1, 1, 1);
        final String expected = generator.getPassword();

        assertEquals(32, expected.length());
        assertTrue(Pattern.compile(REGEX).matcher(expected).matches());
    }

    @Test
    void getPasswordContainsFormDefaultParameters() {
        final PasswordGenerator generator = new PasswordGenerator(16, 2, 2, 2);
        final String expected = generator.getPassword();

        assertEquals(16, expected.length());
        assertTrue(Pattern.compile(REGEX).matcher(expected).matches());
    }

    @Test
    void passwordIsTooShortReturnsPasswordException() {
        final PasswordGenerator generator = new PasswordGenerator(7, 2, 2, 2);

        Assertions.assertThrows(PasswordException.class, generator::getPassword,
                "password length must be 8 - 128 characters");
    }

    @Test
    void passwordIsTooLongReturnsPasswordException() {
        final PasswordGenerator generator = new PasswordGenerator(129, 2, 2, 2);

        Assertions.assertThrows(PasswordException.class, generator::getPassword,
                "password length must be 8 - 128 characters");
    }

    @Test
    void passwordParametersLargerThanPasswordReturnsPasswordException() {
        final PasswordGenerator generator = new PasswordGenerator(12, 5, 5, 5);

        Assertions.assertThrows(PasswordException.class, generator::getPassword,
                "requirement fields exceed password length");
    }

    @Test
    void passwordParameterContainsNegativeValueReturnsIllegalArgumentException() {
        final PasswordGenerator generator = new PasswordGenerator(12, -1, 5, 5);

        Assertions.assertThrows(IllegalArgumentException.class, generator::getPassword,
                "invalid field size");
    }

}
