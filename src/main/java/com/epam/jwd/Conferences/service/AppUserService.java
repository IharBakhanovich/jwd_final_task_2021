package com.epam.jwd.Conferences.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dao.*;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.exception.EntityNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This is a service (model) of the application. The singleton.
 */
public class AppUserService implements UserService {

//    private static final String NOT_FOUND_MESSAGE = "User with such a login does no exist. Login: %s";

    private final UserDAO userDAO;
    private final ConferenceDAO conferenceDAO;
    private final ReportDAO reportDAO;
    private final SectionDAO sectionDAO;

    // TODO по хорошему надо создать отдельные классы, которые инкапсулируют доступ к api BCrypt,
    //  чтобы если в будущем мы решим изменить библиотеку - не пришлось переписывать много кода.
    private final BCrypt.Hasher hasher;
    private final BCrypt.Verifyer verifyer;

    private AppUserService() {
        this.userDAO = DAOFactory.getInstance().getUserDAO();
        this.conferenceDAO = DAOFactory.getInstance().getConferenceDAO();
        this.reportDAO = DAOFactory.getInstance().getReportDAO();
        this.sectionDAO = DAOFactory.getInstance().getSectionDAO();
        this.hasher = BCrypt.withDefaults();
        this.verifyer = BCrypt.verifyer();
    }

    private static class SimpleUserServiceHolder {
        private final static AppUserService instance
                = new AppUserService();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static AppUserService getInstance() {
        return AppUserService.SimpleUserServiceHolder.instance;
    }

    /**
     * Creates a new user in the system.
     *
     * @param user is the user to create.
     * @throws DuplicateException if there is the user with the such a nickname in the system.
     */
    @Override
    public void createUser(User user) throws DuplicateException {
        // encryptedPassword зашифруем с помощью bcrypt
        // hash-функции необратимы, т.е. расхешировать пароль не получится,
        // поэтому при логине verifier будет хэшировать то, что ввел пользователь
        // и сравнивать с тем, что сохранено (захешировано)


        // перед тем как сохранить пользователя придется его создать

        final char[] rawPassword = user.getPassword().toCharArray();

        final String encryptedPassword = hasher.hashToString(MIN_COST, rawPassword);
        User userToSave
                = new User(user.getEmail(), encryptedPassword,
                user.getSalt(), user.getNumberLoginAttempts(), user.getVerificationToken(),
                user.isEmailVerified(), user.getNickname(), user.getFirstName(),
                user.getSurname(), user.getRole());
        userDAO.save(userToSave);
    }

    /**
     * Checks if the user can login in the system.
     *
     * @param user is the {@link User} to login in the system.
     * @return true if the {@param user} can login.
     */
    @Override
    public boolean canLogIn(User user) {
        //сначала хэшируем пароль. Даже если мы не найдем впоследствии пользователя в базе данных,
        // мы хотим защититься от тайминг-атаки, которая направлена на сравнение времени:
        // если бы мы делали это после поиска юзера, то отследив потраченное время на респонз
        // (примечание: хеширование тратит какое-то время),
        // можно было бы выяснить какие пользователи на сайте есть, а каких нет, потому что для одних ответ,
        // что пароль не подходит приходил бы достаточно быстро (валилось бы сразу на findByLogin),
        // а для других чуть медленнее (проходило бы findByLogin, а потом пыталась бы захешироваться).
        // Поэтому сразу хешируемся.
        final byte[] enteredPassword = user.getPassword().getBytes(UTF_8);
        //final String encryptedPassword = hasher.hashToString(MIN_COST, rawPassword);
        //затем ищем пользователя в системе
        try {
            final User persistedUser = this.findByLogin(user.getNickname());
            final byte[] encryptedPasswordFromDb = persistedUser.getPassword().getBytes(UTF_8);
            boolean result = verifyer.verify(enteredPassword, encryptedPasswordFromDb).verified;
            return result;
        } catch (EntityNotFoundException exception) {
            return false;
        }
    }

    /**
     * Finds {@link User} by its nickname.
     *
     * @param login is the nickname of the user to find.
     * @return {@link User}
     */
    @Override
    public User findByLogin(String login) {
        return userDAO.findUserByNickname(login)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ApplicationConstants.NOT_FOUND_MESSAGE, login)));
    }

    /**
     * Returns all {@link User}s in the system.
     *
     * @return {@link List<User>} that contains all users in the system.
     */
    @Override
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public void clean() {
        //this.userDAO.clean();
    }

    /**
     * Returns all {@link Conference}es in the system.
     *
     * @return {@link List<Conference>} that contains all conferences in the system.
     */
    @Override
    public List<Conference> findAllConferences() {
        return conferenceDAO.findAll();
    }

    /**
     * Returns all the {@link Section}s of the {@link Conference} with the id equals {@param id}.
     *
     * @param id is the id of the {@link Conference} which {@link Section}s are to return.
     * @return all the {@link Section}s of the {@link Conference} with the id equals {@param id}.
     */
    @Override
    public List<Section> findAllSectionsByConferenceID(Long id) {
        return sectionDAO.findAllSectionsByConferenceID(id);
    }

    /**
     * Returns all the {@link Report}s of the {@link Section} with the id equals {@param sectionId}
     * and the {@link Conference} with the id equals {@param conferenceId}.
     *
     * @param sectionId    is the id of the {@link Section} which {@link Report}s are to return.
     * @param conferenceId is the id of the {@link Conference} which {@link Report}s are to return.
     * @return all the {@link Report}s of the {@link Section} with the id equals {@param sectionId}
     * and the {@link Conference} with the id equals {@param conferenceId}.
     */
    @Override
    public List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId) {
        return reportDAO.findAllReportsBySectionID(sectionId, conferenceId);
    }

    /**
     * Returns {@link Optional<User>} that contains {@link User} with the id equals {@param id} and null
     * if there is no {@link User} with the id equals {@param id} in the system.
     *
     * @param id is the value of the id of the {@link User} to find.
     * @return {@link Optional<User>} that contains {@link User} with the id equals {@param id} and null
     * if there is no {@link User} with the id equals {@param id} in the system.
     */
    @Override
    public Optional<User> findUserByID(Long id) {
        return userDAO.findById(id);
    }

    /**
     * Returns {@link Optional<Report>} that contains {@link Report} with the id equals {@param id} and null
     * if there is no {@link Report} with the id equals {@param id} in the system.
     *
     * @param id is the value of the id of the {@link Report} to find.
     * @return {@link Optional<Report>} that contains {@link Report} with the id equals {@param id} and null
     * if there is no {@link Report} with the id equals {@param id} in the system.
     */
    @Override
    public Optional<Report> findReportByID(Long id) {
        return reportDAO.findById(id);
    }

    /**
     * Returns all {@link Section}s in the system.
     *
     * @return {@link List<Section>} that contains all sections in the system.
     */
    @Override
    public List<Section> findAllSections() {
        return sectionDAO.findAll();
    }

    /**
     * Updates the {@link User}.
     *
     * @param userToUpdate is the {@link User} to update.
     */
    @Override
    public void updateUser(User userToUpdate) {
        userDAO.update(userToUpdate);
    }

    /**
     * Updates the {@link Report}.
     *
     * @param reportToUpdate is the {@link Report} to update.
     */
    @Override
    public void updateReport(Report reportToUpdate) {
        reportDAO.update(reportToUpdate);
    }

    /**
     * Creates a new {@link Conference} in the system.
     *
     * @param conferenceToCreate is the {@link Conference} to create.
     * @throws DuplicateException if there is the {@link Conference}
     *                            with the conferenceTitle of the {@param conferenceToCreate}
     */
    @Override
    public void createConference(Conference conferenceToCreate) throws DuplicateException {
        conferenceDAO.save(conferenceToCreate);
    }

    /**
     * Creates a new {@link Section} in the system.
     *
     * @param sectionToCreate is the {@link Section} to create.
     * @throws DuplicateException if there is the {@link Section}
     *                            with the sectionName of the {@param sectionToCreate}
     *                            and there is a violation of unique constrains in the database.
     */
    @Override
    public void createSection(Section sectionToCreate) throws DuplicateException {
        sectionDAO.save(sectionToCreate);
    }

    /**
     * Creates a new {@link Report} in the system.
     *
     * @param reportToCreate is the {@link Report} to create.
     * @throws DuplicateException if there is the {@link Section}
     *                            with the sectionName of the {@param reportToCreate}
     *                            and there is a violation of unique constrains in the database.
     */
    @Override
    public void createReport(Report reportToCreate) throws DuplicateException {
        reportDAO.save(reportToCreate);
    }

    /**
     * Updates the {@link Conference}.
     *
     * @param conferenceToUpdate is the {@link Conference} to update.
     */
    @Override
    public void updateConference(Conference conferenceToUpdate) {
        conferenceDAO.update(conferenceToUpdate);
    }

    /**
     * Updates the {@link Section}.
     *
     * @param sectionToUpdate is the {@link Section} to update.
     */
    @Override
    public void updateSection(Section sectionToUpdate) {
        sectionDAO.update(sectionToUpdate);
    }

    /**
     * Finds all {@link Report}s in the system that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.QUESTION, which are inherited to the sections which are managed by the user with id
     * equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} which questions are found.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the ReportType.QUESTION,
     * which are inherited to the sections which are managed by the user with id equals {@param managerId}.
     */
    @Override
    public List<Report> findAllQuestions(Long managerId) {
        return reportDAO.findAllQuestionsByManagerId(managerId);
    }

    /**
     * Finds all the {@link Report}s in the system that have the parameter questionReportId equals to the value
     * of the {@param questionReportId}
     * @param questionReportId is the {@link Long} value of the parameter questionReportId of the Report.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the value of the parameter
     * questionReportId equals to the {@param questionReportId}.
     */
    @Override
    public List<Report> findAllReportsByQuestionId(Long questionReportId) {
        Optional<Report> question = reportDAO.findById(questionReportId);
        List<Report> answers = reportDAO.findAllReportsByQuestionReportId(questionReportId);

        if (question.isPresent()) {
            final Report questionToAdd = new Report(question.get().getId(), question.get().getSectionId(),
                    question.get().getConferenceId(), question.get().getReportText(), question.get().getReportType(),
                    question.get().getApplicant(), question.get().getQuestionReportId());
            answers.add(questionToAdd);
            Collections.sort(answers);
        }
        return answers;
    }

    /**
     * Sets a new {@link Role} to the {@link User} with id equals {@param userId}
     *
     * @param userId is the id of the {@link User} which {@link Role} is to update.
     * @param newRole is the {@link Role} to set.
     */
    @Override
    public void updateUserRole(Long userId, Long newRole) {
        userDAO.updateUserRoleByUserId(userId, newRole);
    }

    /**
     * Returns all {@link Report}s in the system.
     *
     * @return {@link List<Report>} that contains all reports in the system.
     */
    @Override
    public List<Report> findAllReports() {
        return reportDAO.findAll();
    }

    /**
     * Finds all {@link Report}s in the system that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.APPLICATION, which are inherited to the sections which are managed by the user with id
     * equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} which questions are found.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the ReportType.APPLICATION,
     * which are inherited to the sections which are managed by the user with id equals {@param managerId}.
     */
    @Override
    public List<Report> findAllApplications(Long managerId) {
        return reportDAO.findAllApplicationsByManagerId(managerId);
    }

    /**
     * Finds all {@link Report}s in the system that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.APPLICATION, which were created by a {@link User} with the id equals {@param applicantId}.
     *
     * @param applicantId is the {@link Long} that is id of the {@link User} application created by him to found.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the ReportType.APPLICATION,
     * which are created by the {@link User} with id equals {@param applicantId}.
     */
    @Override
    public List<Report> findApplicantApplications(Long applicantId) {
        return reportDAO.findAllApplicationsByApplicantId(applicantId);
    }

    /**
     * Deletes the {@link Report}.
     *
     * @param reportId is the id value of the {@link Report} to delete.
     */
    @Override
    public void deleteReport(Long reportId) {
        reportDAO.delete(reportId);
    }

    /**
     * Finds all {@link Report}s in the system that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.QUESTION, which were created by a {@link User} with the id equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} questions created by him to found.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the ReportType.QUESTION,
     * which are created by the {@link User} with id equals {@param managerId}.
     */
    @Override
    public List<Report> findApplicantQuestions(Long managerId) {
        return reportDAO.findAllQuestionsByApplicantId(managerId);
    }
}
