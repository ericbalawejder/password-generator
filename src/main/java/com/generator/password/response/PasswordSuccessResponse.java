package com.generator.password.response;

import com.generator.password.entity.PasswordGenerator;

public class PasswordSuccessResponse {

    private final int length;
    private final int uppercase;
    private final int digit;
    private final int specialChar;
    private final String password;

    public PasswordSuccessResponse(PasswordGenerator generator) {
        this.length = generator.getLength();
        this.uppercase = generator.getUppercase();
        this.digit = generator.getDigit();
        this.specialChar = generator.getSpecialChar();
        this.password = generator.getPassword();
    }

    public int getLength() {
        return length;
    }

    public int getUppercase() {
        return uppercase;
    }

    public int getDigit() {
        return digit;
    }

    public int getSpecialChar() {
        return specialChar;
    }

    public String getPassword() {
        return password;
    }

}
