package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = TagConverter.class)
public interface GiftCertificateConverter extends GeneralEntityConverter<GiftCertificateDto, GiftCertificate, Long> {

    @Mappings({
            @Mapping(target = "createDate", source = "createDate", defaultExpression = "java(java.time.Instant.now())"),
            @Mapping(target = "updateDate", source = "updateDate", defaultExpression = "java(java.time.Instant.now())"),
            @Mapping(target = "isDeleted", source = "isDeleted", defaultExpression = "java(java.lang.Boolean.FALSE)"),
            @Mapping(target = "tags", ignore = true),
    })
    @Override
    GiftCertificate convertToDomain(GiftCertificateDto dto);

    @Override
    GiftCertificateDto convertToDto(GiftCertificate certificate);

    @Mappings({
            @Mapping(target = "updateDate", expression = "java(java.time.Instant.now())"),
    })
    GiftCertificate convertToUpdatingDomain(GiftCertificate sourceDomain);

}
