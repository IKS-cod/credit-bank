package com.neoflex.calculator.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateEmailBorrowerTest {

    private ValidateEmailBorrower validator;

    @BeforeEach
    void setUp() {
        String emailRegex = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$";
        validator = new ValidateEmailBorrower(emailRegex);
    }

    @Test
    @DisplayName("validateEmailBorrower returns false for null email")
    void testNullEmail() {
        assertFalse(validator.validateEmailBorrower(null));
    }

    @Test
    @DisplayName("validateEmailBorrower returns false for empty string")
    void testEmptyEmail() {
        assertFalse(validator.validateEmailBorrower(""));
    }

    @Test
    @DisplayName("validateEmailBorrower returns false for invalid email formats")
    void testInvalidEmails() {
        assertFalse(validator.validateEmailBorrower("plainaddress"));
        assertFalse(validator.validateEmailBorrower("@missingusername.com"));
        assertFalse(validator.validateEmailBorrower("username@domain,com"));
        assertFalse(validator.validateEmailBorrower("username@domain com"));
    }

    @Test
    @DisplayName("validateEmailBorrower returns true for valid email formats")
    void testValidEmails() {
        assertTrue(validator.validateEmailBorrower("user@example.com"));
        assertTrue(validator.validateEmailBorrower("user_name-123@example-domain.com"));
        assertTrue(validator.validateEmailBorrower("user+mailbox@example.com"));
        assertTrue(validator.validateEmailBorrower("user!#$%&'*+/=?`{|}~^.-@example.com"));
    }
}
