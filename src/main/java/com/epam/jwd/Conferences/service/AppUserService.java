package com.epam.jwd.Conferences.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.Conferences.dao.*;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This is a service (model) of the application. The singleton.
 */
public class AppUserService implements UserService {

    private static final String NOT_FOUND_MESSAGE = "User with such a login does no exist. Login: %s";
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


    @Override
    public void create(User user) {
        // encryptedPassword зашифруем с помощью bcrypt
        // hash-функции необратимы, т.е. расхешировать пароль не получится,
        // поэтому при логине verifier будет хэшировать то, что ввел пользователь
        // и сравнивать с тем, что сохранено (захешировано)


        // перед тем как сохранить пользователя придется его создать

        final char[] rawPassword = user.getPassword().toCharArray();

        final String encryptedPassword = hasher.hashToString(MIN_COST, rawPassword);
        //TODO сделать конструктор с автозаполнением некоторых полей - простой конструктор
        User userToSave
                = new User(user.getEmail(), encryptedPassword,
                user.getSalt(), user.getNumberLoginAttempts(), user.getVerificationToken(),
                user.isEmailVerified(), user.getNickname(), user.getFirstName(),
                user.getSurname(), user.getRole());
        userDAO.save(userToSave);
    }

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

    @Override
    public User findByLogin(String login) {
        return userDAO.findUserByNickname(login)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, login)));
    }

    @Override
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public void clean() {
        //this.userDAO.clean();
    }

    @Override
    public List<Conference> findAllConferences() {
        return conferenceDAO.findAll();
    }

    @Override
    public List<Section> findAllSectionsByConferenceID(Long id) {
        return SectionDAO.retrieve().findAllSectionsByConferenceID(id);
    }

    @Override
    public List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId) {
        return ReportDAO.retrieve().findAllReportsBySectionID(sectionId, conferenceId);
    }

    @Override
    public Optional<User> findUserByID(Long id) {
        return UserDAO.retrieve().findById(id);
    }

    @Override
    public Optional<Report> findReportByID(Long id) {
        return ReportDAO.retrieve().findById(id);
    }

    @Override
    public List<Section> findAllSections() {
        return SectionDAO.retrieve().findAll();
    }
}
