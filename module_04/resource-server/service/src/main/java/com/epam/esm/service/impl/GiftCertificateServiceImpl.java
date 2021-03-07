package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.GiftCertificateUpdateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository_new.impl.GiftCertificateRepository;
import com.epam.esm.repository_new.impl.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.converter.impl.TagConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.EmptyUpdateException;
import com.epam.esm.service.exception.IllegalGiftCertificateUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            GiftCertificate createdCertificate = receiveDomainWhichIsToBeUpdated(createdId);
            createdCertificate.setTags(tagsToLink);
            giftCertificateRepository.save(createdCertificate);
        }

        return createdId;
    }

    @Transactional
    @Override
    public GiftCertificateDto update(GiftCertificateUpdateDto dto) {
        if (dto.getPrice() != null && dto.getDuration() != null) {
            throw new IllegalGiftCertificateUpdateException();
        }

        GiftCertificate sourceDomain = receiveDomainWhichIsToBeUpdated(dto.getId());
        boolean areAssociationsWithTagsUpdated = updateAssociationsWithTags(sourceDomain, dto);
        GiftCertificate targetDomain = receiveUpdatingDomain(sourceDomain, dto);
        checkIfUpdatingIsPossibleOrThrow(sourceDomain, targetDomain, areAssociationsWithTagsUpdated);

        return converter.convertToDto(giftCertificateRepository.save(targetDomain));
    }

    private boolean updateAssociationsWithTags(GiftCertificate sourceDomain, GiftCertificateUpdateDto dto) {
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            Set<Tag> updatingTags = receiveUpdatingTags(sourceDomain, dto);

            if (!updatingTags.isEmpty()) {
                refreshStateOfTagsIfExistOtherwiseCreate(updatingTags);

                Set<Tag> sourceTags = sourceDomain.getTags();
                sourceTags.addAll(updatingTags);
                sourceDomain.setTags(sourceTags);
                giftCertificateRepository.save(sourceDomain);

                return true;
            }
        }
        return false;
    }

    @Override
    protected Specification<GiftCertificate> assembleJpaSpecification(SearchCriteriaDto<GiftCertificate>
                                                                              searchCriteria) {
//        if (searchCriteria == null) {
//            return new GiftCertificateSpecification(null, null, null, null, null, defaultPageSize);
//        }
//
//        if (searchCriteria.getClass() != GiftCertificateSearchCriteriaDto.class) {
//            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
//        }
//
//        GiftCertificateSearchCriteriaDto params = (GiftCertificateSearchCriteriaDto) searchCriteria;
//        Integer pageSize = params.getPageSize() == null ? defaultPageSize : params.getPageSize();
//        return new GiftCertificateSpecification(params.getTags(), params.getNamePart(), params.getDescriptionPart(),
//                params.getSortParams(), params.getPage(), pageSize);
        return null;
    }

    @Override
    protected Example<GiftCertificate> receiveUniqueConstraints(GiftCertificateDto dto) {
//        Map<String, Object> uniqueConstrains = new HashMap<>();
//        uniqueConstrains.putIfAbsent("name", dto.getName());
//        uniqueConstrains.putIfAbsent("description", dto.getDescription());
//        uniqueConstrains.putIfAbsent("price", dto.getPrice());
//        uniqueConstrains.putIfAbsent("duration", dto.getDuration());
//        return uniqueConstrains;
        return null;
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
                .collect(Collectors.partitioningBy(tag -> {
                    tag.setIsDeleted(Boolean.FALSE);
                    return tagRepository.exists(Example.of(tag));
                }));

        List<Tag> tagsToRefresh = separatedTags.get(Boolean.TRUE);
        refreshStateOfTagsByTheirName(tagsToRefresh);

        List<Tag> tagsToCreate = separatedTags.get(Boolean.FALSE);
        tagsToCreate.forEach(tagRepository::save);
    }

    private void refreshStateOfTagsByTheirName(List<Tag> tagsToRefresh) {
        tagsToRefresh.forEach(tag -> {
            Long tagId = tagRepository.findIdByNameAndIsDeleted(tag.getName(), Boolean.FALSE);
            // TODO verify
//            Long tagId = entityManager.createQuery("SELECT t.id FROM Tag t WHERE t.name=:name " +
//                    "AND t.isDeleted=:isDeleted", Long.class)
//                    .setParameter("name", tag.getName())
//                    .setParameter("isDeleted", Boolean.FALSE)
//                    .getSingleResult();
            tag.setId(tagId);
        });
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
