package com.epam.esm.service.old.converter;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;

import java.time.Instant;

public class GiftCertificateConverter implements EntityConverter<GiftCertificateDto, GiftCertificate, Long> {

    @Override
    public GiftCertificate convertToDomain(GiftCertificateDto dto) {
        return GiftCertificate.builder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setPrice(dto.getPrice())
                .setDuration(dto.getDuration())
                .setCreateDate(dto.getId() == null ? Instant.now() : null)
                .setUpdateDate(Instant.now())
                .build();
    }

    @Override
    public GiftCertificateDto convertToDto(GiftCertificate domain) {
        return GiftCertificateDto.builder()
                .setId(domain.getId())
                .setName(domain.getName())
                .setDescription(domain.getDescription())
                .setPrice(domain.getPrice())
                .setDuration(domain.getDuration())
                .setCreateDate(domain.getCreateDate())
                .setUpdateDate(domain.getUpdateDate())
                .build();
    }

    @Override
    public GiftCertificate convertToUpdatingDomain(GiftCertificate sourceDomain, GiftCertificateDto targetDto) {
        GiftCertificate updatingCertificate = new GiftCertificate();
        updatingCertificate.setId(targetDto.getId());
        updatingCertificate.setUpdateDate(Instant.now());

        if ((targetDto.getName() != null) && !targetDto.getName().equals(sourceDomain.getName())) {
            updatingCertificate.setName(targetDto.getName());
        }

        if ((targetDto.getDescription() != null) && !targetDto.getDescription().equals(sourceDomain.getDescription())) {
            updatingCertificate.setDescription(targetDto.getDescription());
        }

        if ((targetDto.getPrice() != null) && !targetDto.getPrice().equals(sourceDomain.getPrice())) {
            updatingCertificate.setPrice(targetDto.getPrice());
        }

        if ((targetDto.getDuration() != null) && !targetDto.getDuration().equals(sourceDomain.getDuration())) {
            updatingCertificate.setDuration(targetDto.getDuration());
        }

        return updatingCertificate;
    }
}
