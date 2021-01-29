package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecification;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.converter.impl.DefaultTagConverter;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultGiftCertificateService extends GeneralCrudService<GiftCertificateDto, GiftCertificate, Long>
        implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final DefaultTagConverter tagConverter;

    @Autowired
    protected DefaultGiftCertificateService(GeneralEntityConverter<GiftCertificateDto, GiftCertificate, Long> converter,
                                            GiftCertificateRepository giftCertificateRepository, DefaultTagConverter tagConverter) {
        super(giftCertificateRepository, converter);
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public Long create(GiftCertificateDto dto) {
        Long createdId = super.create(dto);
        Set<Tag> tagsToLink = dto.getTags().stream()
                .map(tagConverter::convertToDomain)
                .collect(Collectors.toSet());

        giftCertificateRepository.linkGiftCertificateWithTags(createdId, tagsToLink);
        return createdId;
    }

    @Override
    protected JpaSpecification<GiftCertificate, Long> getDataSourceSpecification(SearchCriteriaDto<GiftCertificate> searchCriteria) {
        if (searchCriteria == null) {
            return new GiftCertificateSpecification();
        }

        if (searchCriteria.getClass() != GiftCertificateSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        GiftCertificateSearchCriteriaDto params = (GiftCertificateSearchCriteriaDto) searchCriteria;
        return new GiftCertificateSpecification(params.getTags(), params.getNamePart(), params.getDescriptionPart(),
                params.getSortParams(), params.getPage());
    }

    @Override
    protected Map<String, Object> receiveUniqueConstraints(GiftCertificateDto dto) {
        Map<String, Object> uniqueConstrains = new HashMap<>();
        uniqueConstrains.putIfAbsent("name", dto.getName());
        uniqueConstrains.putIfAbsent("description", dto.getDescription());
        uniqueConstrains.putIfAbsent("price", dto.getPrice());
        uniqueConstrains.putIfAbsent("duration", dto.getDuration());
        return uniqueConstrains;
    }

    @Override
    protected GiftCertificate receiveUpdatingDomain(GiftCertificate domain, GiftCertificateDto dto) {

        return null; // fixme
    }
}
