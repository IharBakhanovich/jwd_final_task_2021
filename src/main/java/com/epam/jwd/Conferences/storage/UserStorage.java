package com.epam.jwd.Conferences.storage;

import com.epam.jwd.Conferences.dto.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();

    User save(User user);

    Optional<User> findByName(String name);

    void clean();

    static UserStorage inMemory() {
        return InMemoryUserStorage.INSTANCE;
    }
}
