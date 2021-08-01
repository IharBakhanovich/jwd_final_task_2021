package com.epam.jwd.Conferences.validator;

import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.service.AppSectionService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains implementations of all validator which are used in the application. The singleton.
 */
public class AppValidator implements Validator {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * the private default constructor, to not create the instance of the class with 'new' outside the class.
     */
    private AppValidator() {

    }

    private static class AppValidatorHolder {
        private final static AppValidator instance
                = new AppValidator();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static AppValidator getInstance() {
        return AppValidator.AppValidatorHolder.instance;
    }


    /**
     * Checks whether a String corresponds UTF-8 format.
     *
     * @param toValidate is a String to validate.
     * @return true if the {@param toValidate} is in UTF-8 format.
     */
    @Override
    public boolean isStringValid(String toValidate) {
        byte[] byteArray = toValidate.getBytes();
        return isUTF8(byteArray);
    }

    /**
     * Checks the complexity of the password.
     *
     * @param passwordToCheck is a password to check.
     * @return true if complexity of the {@param passworToCheck}
     * corresponds to the application requirements.
     */
    @Override
    public boolean isPasswordComplex(String passwordToCheck) {
        String password = null;
        password = passwordToCheck.trim();
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }

    /**
     * Checks if a String corresponds to the maximum length.
     *
     * @param stringToValid is a String to validate.
     * @param maxLength     is a maximum value of the String.
     * @return true if length of the {@param stringToValid} not more than {@param maxLength}
     */
    @Override
    public boolean isLengthValid(String stringToValid, int maxLength) {
        return !(stringToValid.length() > maxLength);
    }

    /**
     * Checks whether an email corresponds the pattern of an ordinary e-mail address.
     *
     * @param emailToValidate is a String to validate.
     * @return {@code true} if an {@param emailToValidate} looks like an e-mail.
     */
    @Override
    public boolean isEmailValid(String emailToValidate) {
        String eMail = emailToValidate.trim();
        Matcher matcher = EMAIL_PATTERN.matcher(eMail);
        return matcher.matches();
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}
