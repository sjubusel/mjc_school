package com.epam.esm.service;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.UserDto;

public interface UserService extends CrudService<UserDto, User, Long, UserDto> {

}
