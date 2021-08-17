package com.epam.jwd.Conferences.validator;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Contains implementations of all validator which are used in the application. The singleton.
 */
public class AppValidator implements Validator {

//    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$";
//    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
//    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
//    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

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
        String password;
        password = passwordToCheck.trim();
        Matcher matcher = ApplicationConstants.PASSWORD_PATTERN.matcher(password);
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
        Matcher matcher = ApplicationConstants.EMAIL_PATTERN.matcher(eMail);
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
        if (conferenceId == null) {
            return false;
        }
        final List<Conference> conferences = service.findAllConferences();
        Long conferenceIdFromDatabase = null;
        for (Conference conference : conferences
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
        if (userId == null) {
            return false;
        }
        final List<User> users = service.findAllUsers();
        Long userIdFrom = null;
        for (User user : users
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
        if (role == null) {
            return false;
        }
        List<Role> roles = Role.valuesAsList();
        String checkRole = null;
        for (Role role1 : roles
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
        if (sectionId == null) {
            return false;
        }
        final List<Section> sections = service.findAllSections();
        Long sectionIdFromDatabase = null;
        for (Section section : sections
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
        if (reportId == null) {
            return false;
        }
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
        if (reportTypeName == null) {
            return false;
        }
        List<ReportType> reportTypes = ReportType.valuesAsList();
        String checkReportType = null;
        for (ReportType reportType : reportTypes
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
        if (nickname == null) {
            return false;
        }
        final List<User> users = service.findAllUsers();
        String userNickName = null;
        for (User user : users
        ) {
            if (user.getNickname().equals(nickname)) {
                userNickName = user.getNickname();
            }
        }
        return userNickName != null;
    }

    /**
     * Checks whether a Section with the value of {@param sectionName} exist in the system.
     *
     * @param sectionName is a Long to check.
     * @return {@code true} if there is the Section with {@param sectionName} exists in the system.
     */
    @Override
    public boolean isSectionWithSuchNameExistInSystem(String sectionName) {
        if (sectionName == null) {
            return false;
        }
        final List<Section> sections = service.findAllSections();
        for (Section section : sections
        ) {
            if (section.getSectionName().equals(sectionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a Conference with the value of {@param conferenceTitle} exist in the system.
     *
     * @param conferenceTitle is a Long to check.
     * @return {@code true} if there is the Conference with {@param conferenceTitle} exists in the system.
     */
    @Override
    public boolean isConferenceWithSuchTitleExistInSystem(String conferenceTitle) {
        if (conferenceTitle == null) {
            return false;
        }
        final List<Conference> conferences = service.findAllConferences();
        for (Conference conference : conferences
        ) {
            if (conference.getConferenceTitle().equals(conferenceTitle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether in the system exist Conference with the value of {@param conferenceTitle}
     * and value of {@param conferenceId}.
     *
     * @param conferenceId    is a Long to check.
     * @param conferenceTitle is a String to check.
     * @return {@code true} if there is in the system the Conference with {@param conferenceTitle}
     * and with the {@param conferenceId}.
     */
    @Override
    public boolean isConferenceTitleAndIdFromTheSameConference(Long conferenceId, String conferenceTitle) {
        if (conferenceTitle == null || conferenceId == null) {
            return false;
        }
        final List<Conference> conferences = service.findAllConferences();
        for (Conference conference : conferences
        ) {
            if (conference.getConferenceTitle().equals(conferenceTitle) && conference.getId().equals(conferenceId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether in the system exist Section with the value of {@param sectionName}
     * and value of {@param sectionId}.
     *
     * @param sectionId   is a Long to check.
     * @param sectionName is a String to check.
     * @return {@code true} if there is in the system the Section with {@param sectionName}
     * and with the {@param sectionId}.
     */
    @Override
    public boolean isSectionNameAndIdFromTheSameSection(Long sectionId, String sectionName) {
        if (sectionId == null || sectionName == null) {
            return false;
        }
        final List<Section> sections = service.findAllSections();
        for (Section section : sections
        ) {
            if (section.getSectionName().equals(sectionName) && section.getId().equals(sectionId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a Role with the value of {@param role} exist in the system.
     *
     * @param role is a Role to check.
     * @return {@code true} if there is the Role with {@param role} exists in the system.
     */
    @Override
    public boolean isRoleWithSuchNameExistInSystem(String role) {
        if (role == null) {
            return false;
        }
        List<Role> roles = Role.valuesAsList();
        for (Role role1 : roles
        ) {
            if (role.equals(role1.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether in the system exist User with the value of {@param userId}
     * and value of {@param userRole}.
     *
     * @param userId   is a String to check.
     * @param userRole is a String to check.
     * @return {@code true} if there is in the system the User with {@param userId}
     * and with the {@param userRole}.
     */
    @Override
    public boolean isUserIdAndUserRoleFromTheSameUser(String userId, String userRole) {
        if (userId == null || userRole == null) {
            return false;
        }
        final Long userIdInLong = Long.valueOf(userId);
        Optional<User> user = service.findUserByID(userIdInLong);
        if (!user.isPresent()) {
            return false;
        } else {
            return (user.get().getId().equals(userIdInLong) && user.get().getRole().getName().equals(userRole));
        }
    }

    /**
     * Checks whether a Report with the value of {@param reportText} exist in the system.
     *
     * @param reportText is a String to check.
     * @return {@code true} if there is the Report with {@param reportText} exists in the system.
     */
    @Override
    public boolean isReportWithSuchTextExistInSystem(String reportText) {
        List<Report> reports = service.findAllReports();
        for (Report report: reports
             ) {
            if (report.getReportText().equals(reportText)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}