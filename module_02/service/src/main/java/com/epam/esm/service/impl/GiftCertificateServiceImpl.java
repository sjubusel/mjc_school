package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.SqlSpecification;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecification;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.EntityConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl extends BasicCrudService<GiftCertificateDto, GiftCertificate, Long>
        implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Autowired
    public GiftCertificateServiceImpl(EntityConverter<GiftCertificateDto, GiftCertificate, Long> entityConverter,
                                      GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository,
                                      ServiceValidator<GiftCertificateDto, GiftCertificate, Long> validator,
                                      TagConverter tagConverter) {
        super(entityConverter, giftCertificateRepository, validator);
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Transactional
    @Override
    public Long create(GiftCertificateDto certificateDto) {
        Long createdId = super.create(certificateDto);

        List<Tag> updatingTags = certificateDto.getTags().stream()
                .map(tagConverter::convertToDomain)
                .collect(Collectors.toList());
        linkGiftCertificateWithTags(createdId, updatingTags);

        return createdId;
    }

    @Transactional(readOnly = true)
    @Override
    public List<GiftCertificateDto> query(SearchCriteriaDto<GiftCertificate> searchCriteria) {
        List<GiftCertificateDto> plainCertificates = super.query(searchCriteria);

        return plainCertificates.stream()
                .peek(certificateDto -> certificateDto.setTags(
                        receiveListOfLinkedTagDto(certificateDto)
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public GiftCertificateDto findOne(Long certificateId) {
        GiftCertificateDto certificateDto = super.findOne(certificateId);
        certificateDto.setTags(
                receiveListOfLinkedTagDto(certificateDto)
        );
        return certificateDto;
    }

    @Transactional
    @Override
    public void update(GiftCertificateDto targetDto) {
        GiftCertificate updatingGiftCertificate = generateUpdatingDomain(targetDto);

        List<Tag> updatingTags = updatingGiftCertificate.getTags();
        linkGiftCertificateWithTags(targetDto.getId(), updatingTags);

        crudRepository.update(updatingGiftCertificate);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        giftCertificateRepository.deleteLinkBetweenGiftCertificateAndTags(id);
        super.delete(id);
    }

    @Override
    protected String getMainUniqueEntityValue(GiftCertificateDto certificateDto) {
        return certificateDto.getName();
    }

    @Override
    protected SqlSpecification getSqlSpecification(SearchCriteriaDto<GiftCertificate> criteria) {
        if (criteria == null) {
            return new GiftCertificateSpecification();
        }
        if (criteria.getClass() != GiftCertificateSearchCriteriaDto.class) {
            throw new RuntimeException(); // FIXME
        }
        GiftCertificateSearchCriteriaDto searchCriteria = (GiftCertificateSearchCriteriaDto) criteria;
        return new GiftCertificateSpecification(searchCriteria.getTags(), searchCriteria.getName(),
                searchCriteria.getDescription(), searchCriteria.getSortParams(), searchCriteria.getOrder());
    }

    private List<TagDto> receiveListOfLinkedTagDto(GiftCertificateDto certificateDto) {
        return tagRepository.receiveTagsByGiftCertificateId(certificateDto.getId()).stream()
                .map(tagConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private void linkGiftCertificateWithTags(Long certificateId, List<Tag> updatingTags) {
        updatingTags.stream()
                .filter(tag -> !tagRepository.exists(tag.getName()))
                .forEach(tagRepository::create);
        updatingTags.forEach(tag -> giftCertificateRepository.linkCertificateWithTag(certificateId, tag.getName()));
    }
}
