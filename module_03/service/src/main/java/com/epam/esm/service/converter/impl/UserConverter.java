package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserConverter extends GeneralEntityConverter<UserDto, User, Long> {

    @Override
    User convertToDomain(UserDto dto);

    @Override
    UserDto convertToDto(User user);
}
