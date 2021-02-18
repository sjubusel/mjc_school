package com.epam.esm.service.security;

import com.epam.esm.service.dto.impl.UserCredentialsDto;

public interface AuthenticationService {

    String singIn(UserCredentialsDto credentials);

}
