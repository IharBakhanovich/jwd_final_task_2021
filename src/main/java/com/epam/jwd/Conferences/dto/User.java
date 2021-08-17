package com.epam.jwd.Conferences.dto;

import java.util.Objects;

import static com.epam.jwd.Conferences.dto.Role.USER;

/**
 * Stores the User.
 */
public class User implements DatabaseEntity<Long> {
    private final Long id;
    private final String email;
    private final String password;
    private final String salt;
    private final int numberLoginAttempts;
    private final String verificationToken;
    private final boolean emailVerified;
    private final String nickname;
    private final String firstName;
    private final String surname;
    private final Role role;

    /**
     * Constructs a new {@link User}.
     *
     * @param id                  is the value of the id of the new {@link User}
     * @param email               is the value of the email of the new {@link User}
     * @param password            is the value of the password of the new {@link User}
     * @param salt                is the value of the salt of the new {@link User}
     * @param numberLoginAttempts is the value of the numberLoginAttempts of the new {@link User}
     * @param verificationToken   is the value of the the verificationToken of the new {@link User}
     * @param emailVerified       is the value of the emailVerified of the new {@link User}
     * @param nickname            is the value of the nickname of the new {@link User}
     * @param firstName           is the value of the firstName of the new {@link User}
     * @param surname             is the value of the surname of the new {@link User}
     * @param role                is the value of the role of the new {@link User}
     */
    public User(Long id, String email, String password,
                String salt, int numberLoginAttempts, String verificationToken,
                boolean emailVerified, String nickname, String firstName,
                String surname, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.numberLoginAttempts = numberLoginAttempts;
        this.verificationToken = verificationToken;
        this.emailVerified = emailVerified;
        this.nickname = nickname;
        this.firstName = firstName;
        this.surname = surname;
        this.role = role;
    }

    /**
     * Constructs a new {@link User}.
     *
     * @param email               is the value of the email of the new {@link User}
     * @param password            is the value of the password of the new {@link User}
     * @param salt                is the value of the salt of the new {@link User}
     * @param numberLoginAttempts is the value of the numberLoginAttempts of the new {@link User}
     * @param verificationToken   is the value of the the verificationToken of the new {@link User}
     * @param emailVerified       is the value of the emailVerified of the new {@link User}
     * @param nickname            is the value of the nickname of the new {@link User}
     * @param firstName           is the value of the firstName of the new {@link User}
     * @param surname             is the value of the surname of the new {@link User}
     * @param role                is the value of the role of the new {@link User}
     */
    public User(String email, String password,
                String salt, int numberLoginAttempts, String verificationToken,
                boolean emailVerified, String nickname, String firstName,
                String surname, Role role) {
        this(null, email, password, salt,
                numberLoginAttempts, verificationToken, emailVerified,
                nickname, firstName, surname, role);
    }

    /**
     * Constructor to make a logIn.
     */
    public User(String nickname, String password) {
        this(null, "default@email.com", password, null,
                0, null, false,
                nickname, null, null, USER);
    }


    /**
     * @return the userID.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * A Getter for the user's password.
     *
     * @return The user's name.
     */
    public String getPassword() {
        return password;
    }

    /**
     * A Getter for the user's role.
     *
     * @return The user's role.
     */
    public Role getRole() {
        return role;
    }

    /**
     * A Getter for the user's email.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * A Getter for the user's salt.
     *
     * @return The user's salt.
     */
    public String getSalt() {
        return salt;
    }

    /**
     * A Getter for the user's numberLoginAttempts.
     *
     * @return The user's numberLoginAttempts.
     */
    public int getNumberLoginAttempts() {
        return numberLoginAttempts;
    }

    /**
     * A Getter for the user's verificationToken.
     *
     * @return The user's verificationToken.
     */
    public String getVerificationToken() {
        return verificationToken;
    }

    /**
     * A Getter for the user's emailVerified.
     *
     * @return The user's emailVerified.
     */
    public boolean isEmailVerified() {
        return emailVerified;
    }

    /**
     * A Getter for the user's nickname.
     *
     * @return The user's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * A Getter for the user's firstName.
     *
     * @return The user's firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * A Getter for the user's surname.
     *
     * @return The user's surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return numberLoginAttempts == user.numberLoginAttempts
                && emailVerified == user.emailVerified
                && id.equals(user.id)
                && email.equals(user.email)
                && password.equals(user.password)
                && salt.equals(user.salt)
                && verificationToken.equals(user.verificationToken)
                && nickname.equals(user.nickname)
                && firstName.equals(user.firstName)
                && surname.equals(user.surname)
                && role == user.role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, salt, numberLoginAttempts,
                verificationToken, emailVerified, nickname, firstName, surname, role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", numberLoginAttempts=" + numberLoginAttempts +
                ", verificationToken='" + verificationToken + '\'' +
                ", emailVerified=" + emailVerified +
                ", nickname='" + nickname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role +
                '}';
    }
}