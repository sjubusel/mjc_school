package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.UserAuthority;
import com.epam.esm.model.dto.UserAuthorityDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserAuthorityConverter extends GeneralEntityConverter<UserAuthorityDto, UserAuthority, Long> {

    @Mappings({
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "role", ignore = true)
    })
    @Override
    UserAuthority convertToDomain(UserAuthorityDto dto);

    @Override
    UserAuthorityDto convertToDto(UserAuthority userAuthority);
}
