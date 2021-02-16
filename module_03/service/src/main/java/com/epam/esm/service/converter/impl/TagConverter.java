package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TagConverter extends GeneralEntityConverter<TagDto, Tag, Long> {

    @Mappings({
            @Mapping(target = "isDeleted", source = "isDeleted", defaultExpression = "java(java.lang.Boolean.FALSE)"),
            @Mapping(target = "giftCertificates", ignore = true)
    })
    @Override
    Tag convertToDomain(TagDto dto);

    @Mapping(target = "giftCertificates", ignore = true)
    @Override
    TagDto convertToDto(Tag tag);
}
