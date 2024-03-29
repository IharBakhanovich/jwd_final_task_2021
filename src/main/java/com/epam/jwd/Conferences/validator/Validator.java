package com.epam.jwd.Conferences.validator;

import com.epam.jwd.Conferences.dto.Role;

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

    /**
     * Checks whether a Conference with the value of {@param conferenceId} exist in the system.
     *
     * @param conferenceId is a Long to check.
     * @return {@code true} if there is the Conference with {@param conferenceId} exists in the system.
     */
    boolean isConferenceExistInSystem(Long conferenceId);

    /**
     * Checks whether a User with the value of {@param userId} exist in the system.
     *
     * @param userId is a Long to check.
     * @return {@code true} if there is the User with {@param userId} exists in the system.
     */
    boolean isUserWithIdExistInSystem(Long userId);

    /**
     * Checks whether a Role with the value of {@param role} exist in the system.
     *
     * @param role is a Role to check.
     * @return {@code true} if there is the Role with {@param role} exists in the system.
     */
    boolean isRoleExistInSystem(Role role);

    /**
     * Checks whether a Section with the value of {@param sectionId} exist in the system.
     *
     * @param sectionId is a Long to check.
     * @return {@code true} if there is the Section with {@param userId} exists in the system.
     */
    boolean isSectionExistInSystem(Long sectionId);

    /**
     * Checks whether a Report with the value of {@param reportId} exist in the system.
     *
     * @param reportId is a Long to check.
     * @return {@code true} if there is the Report with {@param reportId} exists in the system.
     */
    boolean isReportExistInSystem(Long reportId);

    /**
     * Checks whether a ReportType with the value of {@param reportTypeName} exist in the system.
     *
     * @param reportTypeName is a String to check.
     * @return {@code true} if there is the ReportType with {@param reportTypeName} exists in the system.
     */
    boolean isReportTypeExistInSystem(String reportTypeName);

    /**
     * Checks whether a User with the value of {@param nickname} exist in the system.
     *
     * @param nickmane is a String to check.
     * @return {@code true} if there is the User with {@param nickname} exists in the system.
     */
    boolean isUserWithNicknameExistInSystem(String nickmane);

    /**
     * Checks whether a Section with the value of {@param sectionName} exist in the system.
     *
     * @param sectionName is a Long to check.
     * @return {@code true} if there is the Section with {@param sectionName} exists in the system.
     */
    boolean isSectionWithSuchNameExistInSystem(String sectionName);

    /**
     * Checks whether a Conference with the value of {@param conferenceTitle} exist in the system.
     *
     * @param conferenceTitle is a Long to check.
     * @return {@code true} if there is the Conference with {@param conferenceTitle} exists in the system.
     */
    boolean isConferenceWithSuchTitleExistInSystem(String conferenceTitle);

    /**
     * Checks whether in the system exist Conference with the value of {@param conferenceTitle}
     * and value of {@param conferenceId}.
     *
     * @param  conferenceId is a Long to check.
     * @param conferenceTitle  is a String to check.
     * @return {@code true} if there is in the system the Conference with {@param conferenceTitle}
     *         and with the {@param conferenceId}.
     */
    boolean isConferenceTitleAndIdFromTheSameConference(Long conferenceId, String conferenceTitle);

    /**
     * Checks whether in the system exist Section with the value of {@param sectionName}
     * and value of {@param sectionId}.
     *
     * @param sectionId is a Long to check.
     * @param sectionName is a String to check.
     * @return {@code true} if there is in the system the Section with {@param sectionName}
     *         and with the {@param sectionId}.
     */
    boolean isSectionNameAndIdFromTheSameSection(Long sectionId, String sectionName);

    /**
     * Checks whether a Role with the value of {@param role} exist in the system.
     *
     * @param role is a Role to check.
     * @return {@code true} if there is the Role with {@param role} exists in the system.
     */
    boolean isRoleWithSuchNameExistInSystem(String role);

    /**
     * Checks whether in the system exist User with the value of {@param userId}
     * and value of {@param userRole}.
     *
     * @param  userId is a String to check.
     * @param userRole  is a String to check.
     * @return {@code true} if there is in the system the User with {@param userId}
     *         and with the {@param userRole}.
     */
    boolean isUserIdAndUserRoleFromTheSameUser(String userId, String userRole);

    /**
     * Checks whether a Report with the value of {@param reportText} exist in the system.
     *
     * @param reportText is a String to check.
     * @return {@code true} if there is the Report with {@param reportText} exists in the system.
     */
    boolean isReportWithSuchTextExistInSystem(String reportText);
}
