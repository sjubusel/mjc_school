package com.epam.esm.service.converter;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GiftCertificateConverter implements EntityConverter<GiftCertificateDto, GiftCertificate, Long> {

    private final EntityConverter<TagDto, Tag, Long> tagConverter;

    @Autowired
    public GiftCertificateConverter(EntityConverter<TagDto, Tag, Long> tagConverter) {
        this.tagConverter = tagConverter;
    }


    @Override
    public GiftCertificate convertToDomain(GiftCertificateDto dto) {
        return GiftCertificate.builder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setPrice(dto.getPrice())
                .setDuration(dto.getDuration())
                .setCreateDate(dto.getCreateDate())
                .setUpdateDate(dto.getUpdateDate())
                .setTags(dto.getTags().stream()
                        .map(tagConverter::convertToDomain).collect(Collectors.toList()))
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
                .setTags(domain.getTags().stream()
                        .map(tagConverter::convertToDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public GiftCertificate convertToUpdatingDomain(GiftCertificate sourceDomain, GiftCertificateDto targetDto) {
        GiftCertificate updatingCertificate = new GiftCertificate();
        updatingCertificate.setId(targetDto.getId());

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
