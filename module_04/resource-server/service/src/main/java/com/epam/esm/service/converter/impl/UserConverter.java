package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.domain.UserAuthority;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.other.Role;
import com.epam.esm.service.converter.DefaultUserQualifier;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserAuthorityConverter.class})
public interface UserConverter extends GeneralEntityConverter<UserDto, User, Long> {

    Set<UserAuthority> DEFAULT_USER_AUTHORITY = new HashSet<>(Collections.singleton(new UserAuthority(Role.USER, null)));

    @Mapping(target = "authorities", expression = "java(DEFAULT_USER_AUTHORITY)")
    @Override
    User convertToDomain(UserDto dto);

    @DefaultUserQualifier
    @Mapping(target = "password", ignore = true)
    @Override
    UserDto convertToDto(User user);

    UserDto convertToSecurityDto(User user);
}
