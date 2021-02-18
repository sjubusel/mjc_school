package com.epam.esm.service;

import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.dto.impl.UserCredentialsDto;

public interface AuthenticationService {

    String singIn(UserCredentialsDto credentials);

    Long signUp(UserDto user);
}
