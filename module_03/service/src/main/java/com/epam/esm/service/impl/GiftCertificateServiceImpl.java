package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.GiftCertificateUpdateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecification;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.converter.impl.TagConverter;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.EmptyUpdateException;
import com.epam.esm.service.exception.IllegalGiftCertificateUpdateException;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl extends GeneralCrudService<GiftCertificateDto, GiftCertificate, Long,
        GiftCertificateUpdateDto> implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final GiftCertificateConverter giftCertificateConverter;
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Autowired
    protected GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository,
                                         GiftCertificateConverter giftCertificateConverter,
                                         TagRepository tagRepository,
                                         TagConverter tagConverter) {
        super(giftCertificateRepository, giftCertificateConverter);
        this.giftCertificateRepository = giftCertificateRepository;
        this.giftCertificateConverter = giftCertificateConverter;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Transactional
    @Override
    public Long create(GiftCertificateDto dto) {
        Long createdId = super.create(dto);

        if (dto.getTags() == null) {
            return createdId;
        }

        if (!dto.getTags().isEmpty()) {
            Set<Tag> tagsToLink = dto.getTags().stream()
                    .map(tagConverter::convertToDomain)
                    .collect(Collectors.toSet());
            refreshStateOfTagsIfExistOtherwiseCreate(tagsToLink);
            giftCertificateRepository.linkGiftCertificateWithTags(createdId, tagsToLink);
        }

        return createdId;
    }

    @Transactional
    @Override
    public boolean update(GiftCertificateUpdateDto dto) {
        if (dto.getPrice() != null && dto.getDuration() != null) {
            throw new IllegalGiftCertificateUpdateException();
        }

        GiftCertificate sourceDomain = receiveDomainWhichIsToBeUpdated(dto.getId());
        boolean areAssociationsWithTagsUpdated = updateAssociationsWithTags(sourceDomain, dto);
        GiftCertificate targetDomain = receiveUpdatingDomain(sourceDomain, dto);
        checkIfUpdatingIsPossibleOrThrow(sourceDomain, targetDomain, areAssociationsWithTagsUpdated);

        return giftCertificateRepository.update(targetDomain);
    }

    private boolean updateAssociationsWithTags(GiftCertificate sourceDomain, GiftCertificateUpdateDto dto) {
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            Set<Tag> updatingTags = receiveUpdatingTags(sourceDomain, dto);

            if (!updatingTags.isEmpty()) {
                refreshStateOfTagsIfExistOtherwiseCreate(updatingTags);

                Set<Tag> sourceTags = sourceDomain.getTags();
                sourceTags.addAll(updatingTags);

                giftCertificateRepository.linkGiftCertificateWithTags(sourceDomain.getId(), sourceTags);
                return true;
            }
        }
        return false;
    }

    @Override
    protected JpaSpecification<GiftCertificate, Long> getDataSourceSpecification(SearchCriteriaDto<GiftCertificate>
                                                                                         searchCriteria) {
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
    protected GiftCertificate receiveUpdatingDomain(GiftCertificate domain, GiftCertificateUpdateDto dto) {
        GiftCertificate targetGiftCertificate = giftCertificateConverter.convertToUpdatingDomain(domain);

        if (dto.getPrice() != null) {
            targetGiftCertificate.setPrice(dto.getPrice());
        }

        if (dto.getDuration() != null) {
            targetGiftCertificate.setDuration(dto.getDuration());
        }

        return targetGiftCertificate;
    }

    private void refreshStateOfTagsIfExistOtherwiseCreate(Set<Tag> tagsToLink) {
        Map<Boolean, List<Tag>> separatedTags = tagsToLink.stream()
                .collect(Collectors.partitioningBy(tag -> tagRepository.exists(tag.getName())));

        List<Tag> tagsToRefresh = separatedTags.get(Boolean.TRUE);
        tagRepository.refreshStateOfTagsByTheirName(tagsToRefresh);

        List<Tag> tagsToCreate = separatedTags.get(Boolean.FALSE);
        tagsToCreate.forEach(tagRepository::create);
    }

    private void checkIfUpdatingIsPossibleOrThrow(GiftCertificate sourceDomain, GiftCertificate targetDomain,
                                                  boolean areAssociationsUpdated) {
        if (areGiftCertificatesEqual(sourceDomain, targetDomain) && !areAssociationsUpdated) {
            throw new EmptyUpdateException(sourceDomain.toString());
        }
    }

    private boolean areGiftCertificatesEqual(GiftCertificate sourceDomain, GiftCertificate targetDomain) {
        return sourceDomain.getId().equals(targetDomain.getId())
                && sourceDomain.getName().equals(targetDomain.getName())
                && sourceDomain.getDescription().equals(targetDomain.getDescription())
                && sourceDomain.getPrice().compareTo(targetDomain.getPrice()) == 0
                && sourceDomain.getDuration().equals(targetDomain.getDuration());
    }

    private Set<Tag> receiveUpdatingTags(GiftCertificate sourceDomain, GiftCertificateUpdateDto dto) {
        Set<Tag> domainTags = sourceDomain.getTags();
        Set<TagDto> newTags;

        if (domainTags != null) {
            newTags = dto.getTags().stream()
                    .filter(tagDto -> domainTags.stream()
                            .noneMatch(tag -> tag.getName().equals(tagDto.getName())
                                    && tag.getIsDeleted().equals(Boolean.FALSE)))
                    .collect(Collectors.toSet());
        } else {
            newTags = dto.getTags();
        }

        return newTags.stream().map(tagConverter::convertToDomain).collect(Collectors.toSet());
    }
}
