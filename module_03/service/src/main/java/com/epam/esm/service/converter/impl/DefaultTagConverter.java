package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
public interface DefaultTagConverter extends GeneralEntityConverter<TagDto, Tag, Long> {

    @Mapping(target = "giftCertificates", ignore = true)
    @Override
    Tag convertToDomain(TagDto dto);

    @Mapping(target = "giftCertificates", ignore = true)
    @Override
    TagDto convertToDto(Tag tag);
}
