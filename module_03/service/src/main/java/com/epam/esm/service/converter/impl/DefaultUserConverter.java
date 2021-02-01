package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DefaultUserConverter extends GeneralEntityConverter<UserDto, User, Long> {

    @Mapping(target = "orders", ignore = true)
    @Override
    User convertToDomain(UserDto dto);

    @Mapping(target = "orders", ignore = true)
    @Override
    UserDto convertToDto(User user);
}
