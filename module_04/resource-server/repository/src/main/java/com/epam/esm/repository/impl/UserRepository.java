package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.repository.GeneralCrudRepository;

import java.util.Optional;

public interface UserRepository extends GeneralCrudRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
