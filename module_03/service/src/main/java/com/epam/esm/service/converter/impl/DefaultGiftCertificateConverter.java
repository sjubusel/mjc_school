package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.googlecode.jmapper.JMapper;

public class DefaultGiftCertificateConverter implements GeneralEntityConverter<GiftCertificateDto, GiftCertificate,
        Long> {

    private JMapper<GiftCertificate, GiftCertificateDto> mapperToDomain;
    private JMapper<GiftCertificateDto, GiftCertificate> mapperToDto;

    public DefaultGiftCertificateConverter() {
        this.mapperToDomain = new JMapper<>(GiftCertificate.class, GiftCertificateDto.class);
        this.mapperToDto = new JMapper<>(GiftCertificateDto.class, GiftCertificate.class);
    }


    @Override
    public GiftCertificate convertToDomain(GiftCertificateDto dto) {
        return null;
    }

    @Override
    public GiftCertificateDto convertToDto(GiftCertificate certificate) {
        return null;
    }
}
