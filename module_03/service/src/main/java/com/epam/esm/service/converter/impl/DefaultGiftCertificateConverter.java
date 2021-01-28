package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = DefaultTagConverter.class)
public interface DefaultGiftCertificateConverter extends GeneralEntityConverter<GiftCertificateDto, GiftCertificate,
        Long> {

    @Mappings({
            @Mapping(target = "createDate", source = "createDate", defaultExpression = "java(java.time.Instant.now())"),
            @Mapping(target = "updateDate", source = "updateDate", defaultExpression = "java(java.time.Instant.now())"),
    })
    @Override
    GiftCertificate convertToDomain(GiftCertificateDto dto);

    @Override
    GiftCertificateDto convertToDto(GiftCertificate certificate);
}
