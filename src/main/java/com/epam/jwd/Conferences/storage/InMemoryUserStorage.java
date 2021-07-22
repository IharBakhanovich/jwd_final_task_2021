package com.epam.jwd.Conferences.storage;

import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.BusinessValidationException;
import com.epam.jwd.Conferences.exception.UniqueConstraintViolationException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

public enum InMemoryUserStorage implements UserStorage {
    INSTANCE;

    private static final String DUPLICATE_USER_MSG = "User with such name already exists. Name: %s";
    private static final String UNAUTHORIZED_MSG = "Unauthorized user can not be saved to storage";

    private final Map<Long, User> content;
    private final AtomicLong userAmount;

    InMemoryUserStorage() {
        this.content = new ConcurrentHashMap<>();
        this.userAmount = new AtomicLong(0);
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(content.values());
    }

    @Override
    public User save(User user) {
        // check if such a user already exist in map/DB
        // TODO многопоточность, надо было бы ввести set из имен и проверять его атомарной операцией,
        //  которая сразу после проверки вставит пользователя. Соответственно если получилось вставить,
        //  - делать остальное(класть его в мапу), а если не получилось то кидать эксепшен.
        if (userWithSuchNameAlreadyExists(user.getNickname())) {
            throw new UniqueConstraintViolationException(String.format(DUPLICATE_USER_MSG, user.getNickname()));
        }
        // при попытке добавить неавторизованного пользователя должны сказать, что так делать нельзя
        if (Role.UNAUTHORIZED.equals(user.getRole())) {
            throw new BusinessValidationException(UNAUTHORIZED_MSG);
        }
        final long id = userAmount.incrementAndGet();
        return content.put(id, new User(id, user.getEmail(), user.getPassword(),
                user.getSalt(), user.getNumberLoginAttempts(), user.getVerificationToken(), user.isEmailVerified(),
                user.getNickname(), user.getFirstName(), user.getSurname(), user.getRole()));
    }

    @Override
    public Optional<User> findByName(String name) {
        final Predicate<User> userNameEqualsGiven = user -> user.getNickname().equals(name);
        return content.values()
                .stream()
                .filter(userNameEqualsGiven)
                .findAny();
    }

    @Override
    public void clean() {
        this.content.clear();
        userAmount.set(0);
    }

    private boolean userWithSuchNameAlreadyExists(String name) {
        return this.content.values()
                .stream()
                .map(User::getNickname)
                .anyMatch(name::equals);
    }
}

