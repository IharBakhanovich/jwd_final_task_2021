package com.epam.jwd.Conferences.validator;

import com.epam.jwd.Conferences.service.AppSectionService;
import com.epam.jwd.Conferences.service.SectionService;

/**
 * Contains all the application validators.
 */
public interface Validator {

    /**
     * Returns the instance of the implementation of the Validator.
     *
     * @return the instance of the implementation of the Validator.
     */
    static AppValidator retrieve() {
        return AppValidator.getInstance();
    }

    /**
     * Checks whether a String corresponds UTF-8 format.
     *
     * @param toValidate is a String to validate.
     * @return true if the {@param toValidate} is in UTF-8 format.
     */
    boolean isStringValid(String toValidate);

    /**
     * Checks the complexity of the password.
     *
     * @param passwordToCheck is a password to check.
     * @return true if complexity of the {@param passworToCheck}
     *         corresponds to the application requirements.
     */
    boolean isPasswordComplex(String passwordToCheck);

    /**
     * Checks if a String corresponds to the maximum length.
     *
     * @param stringToValid is a String to validate.
     * @param maxLength is a maximum value of the String.
     * @return true if length of the {@param stringToValid} not more than {@param maxLength}
     */
    boolean isLengthValid(String stringToValid, int maxLength);

    /**
     * Checks whether an email corresponds the pattern of an ordinary e-mail address.
     *
     * @param emailToValidate is a String to validate.
     * @return {@code true} if an {@param emailToValidate} looks like an e-mail.
     */
    boolean isEmailValid(String emailToValidate);

}
