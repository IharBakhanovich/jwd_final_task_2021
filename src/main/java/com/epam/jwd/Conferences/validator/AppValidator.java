package com.epam.jwd.Conferences.validator;

import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.AppSectionService;
import com.epam.jwd.Conferences.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    private final UserService service;

    /**
     * the private default constructor, to not create the instance of the class with 'new' outside the class.
     */
    private AppValidator() {
        this.service = UserService.retrieve();
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

    /**
     * Checks whether a Conference with the value of {@param conferenceId} exist in the system.
     *
     * @param conferenceId is a Long to check.
     * @return {@code true} if there is the Conference with {@param conferenceId} exists in the system.
     */
    @Override
    public boolean isConferenceExistInSystem(Long conferenceId) {
        final List<Conference> conferences = service.findAllConferences();
        Long conferenceIdFromDatabase = null;
        for (Conference conference: conferences
             ) {
            if (conference.getId().equals(conferenceId)) {
                conferenceIdFromDatabase = conference.getId();
            }
        }
        return conferenceIdFromDatabase != null;
    }

    /**
     * Checks whether a User with the value of {@param userId} exist in the system.
     *
     * @param userId is a Long to check.
     * @return {@code true} if there is the User with {@param userId} exists in the system.
     */
    @Override
    public boolean isUserWithIdExistInSystem(Long userId) {
        final List<User> users = service.findAllUsers();
        Long userIdFrom = null;
        String userNickName = null;
        for (User user: users
        ) {
            if (user.getId().equals(userId)) {
                userIdFrom = user.getId();
            }
        }
        return userIdFrom != null;
    }

    /**
     * Checks whether a Role with the value of {@param role} exist in the system.
     *
     * @param role is a Role to check.
     * @return {@code true} if there is the Role with {@param role} exists in the system.
     */
    @Override
    public boolean isRoleExistInSystem(Role role) {
        List<Role> roles = Role.valuesAsList();
        String checkRole = null;
        for (Role role1: roles
        ) {
            if (role.getName().equals(role1.getName())) {
                checkRole = role1.getName();
            }
        }
        return checkRole != null;
    }

    /**
     * Checks whether a Section with the value of {@param sectionId} exist in the system.
     *
     * @param sectionId is a Long to check.
     * @return {@code true} if there is the Section with {@param userId} exists in the system.
     */
    @Override
    public boolean isSectionExistInSystem(Long sectionId) {
        final List<Section> sections = service.findAllSections();
        Long sectionIdFromDatabase = null;
        for (Section section: sections
        ) {
            if (section.getId().equals(sectionId)) {
                sectionIdFromDatabase = section.getId();
            }
        }
        return sectionIdFromDatabase != null;
    }

    /**
     * Checks whether a Report with the value of {@param reportId} exist in the system.
     *
     * @param reportId is a Long to check.
     * @return {@code true} if there is the Report with {@param reportId} exists in the system.
     */
    @Override
    public boolean isReportExistInSystem(Long reportId) {
        Optional<Report> report = service.findReportByID(reportId);
        return report.isPresent();
    }

    /**
     * Checks whether a ReportType with the value of {@param reportTypeName} exist in the system.
     *
     * @param reportTypeName is a String to check.
     * @return {@code true} if there is the ReportType with {@param reportTypeName} exists in the system.
     */
    @Override
    public boolean isReportTypeExistInSystem(String reportTypeName) {
        List<ReportType> reportTypes = ReportType.valuesAsList();
        String checkReportType = null;
        for (ReportType reportType: reportTypes
        ) {
            if (reportType.getName().equals(reportTypeName)) {
                checkReportType = reportType.getName();
            }
        }
        return checkReportType != null;
    }

    /**
     * Checks whether a User with the value of {@param nickname} exist in the system.
     *
     * @param nickname is a String to check.
     * @return {@code true} if there is the User with {@param nickname} exists in the system.
     */
    @Override
    public boolean isUserWithNicknameExistInSystem(String nickname) {
        final List<User> users = service.findAllUsers();
        String userNickName = null;
        for (User user: users
        ) {
            if (user.getNickname().equals(nickname)) {
                userNickName = user.getNickname();
            }
        }
        return userNickName != null;
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}