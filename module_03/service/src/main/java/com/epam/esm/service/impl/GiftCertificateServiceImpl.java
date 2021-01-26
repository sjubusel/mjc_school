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
import com.epam.esm.service.old.converter.EntityConverter;
import com.epam.esm.service.old.converter.TagConverter;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import com.epam.esm.service.old.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * a class which realizes business logic of CRUD operations applied to resources called "Gift-certificates"
 */
@Service
public class GiftCertificateServiceImpl extends BasicCrudService<GiftCertificateDto, GiftCertificate, Long>
        implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Autowired
    public GiftCertificateServiceImpl(EntityConverter<GiftCertificateDto, GiftCertificate, Long> entityConverter,
                                      GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository,
                                      ServiceValidator<GiftCertificate, Long> validator,
                                      TagConverter tagConverter) {
        super(entityConverter, giftCertificateRepository, validator);
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    /**
     * a method which is in for creation of a resource in a data source, creation of linked resources of Tags-type
     * in a data source if they don't exist in a data source, and joining of a newly created resource with linked tag
     *
     * @param certificateDto an data transfer object which contains fields of a target entity (gift-certificate)
     *                       and secondary entities (tags)
     * @return an identification number of a newly created resource, which is bound with secondary resources if it is
     * needed
     */
    @Transactional
    @Override
    public Long create(GiftCertificateDto certificateDto) {
        Long createdId = super.create(certificateDto);

        if (certificateDto.getTags() == null) {
            return createdId;
        }

        if (certificateDto.getTags().size() > 0) {
            List<Tag> updatingTags = certificateDto.getTags().stream()
                    .map(tagConverter::convertToDomain)
                    .collect(Collectors.toList());

            createIfNotExist(updatingTags);
            giftCertificateRepository.linkGiftCertificateWithTags(createdId, updatingTags);
        }

        return createdId;
    }

    /**
     * a method which queries target resources (gift-certificate) with linked secondary resources (tags)
     * from a data source according to search criteria (plural): first of all target resources are selected, then
     * secondary resources are queried for each target resources
     *
     * @param searchCriteria an object which stores search criteria
     * @return a collection of data transfer object which represent target resources
     */
    @Transactional(readOnly = true)
    @Override
    public List<GiftCertificateDto> query(SearchCriteriaDto<GiftCertificate> searchCriteria) {
        List<GiftCertificateDto> plainCertificates = super.query(searchCriteria);

        return plainCertificates.stream()
                .peek(certificateDto -> certificateDto.setTags(
                        new HashSet<>(receiveListOfLinkedTagDto(certificateDto))
                )).collect(Collectors.toList());
    }

    /**
     * a method which fetches a target resource (gift-certificate) of a certain ID
     * with linked secondary resources (tags)
     *
     * @param certificateId an identification number of a target resource
     * @return a dto representation of a target resource with secondary ones
     */
    @Transactional(readOnly = true)
    @Override
    public GiftCertificateDto findOne(Long certificateId) {
        GiftCertificateDto certificateDto = super.findOne(certificateId);
        certificateDto.setTags(
                new HashSet<>(receiveListOfLinkedTagDto(certificateDto))
        );
        return certificateDto;
    }

    /**
     * a method which updates state of a target resource: it fetches current state of the resource from a datasource
     * select fields which should be updated and collects secondary resources (tags) which should be linked, then
     * link secondary resources with the resource and in the end modifies only selected fields of the resource
     *
     * @param targetDto an object which contains a mixture of unmodified and modified fields of a target repository and
     *                  already linked or waiting to be linked secondary resources
     * @return true if all modifications are successfully performed
     */
    @Transactional
    @Override
    public boolean update(GiftCertificateDto targetDto) {
        GiftCertificate updatingGiftCertificate = receiveUpdatingDomain(targetDto);

        if (targetDto.getTags() != null) {
            List<Tag> sourceTags = tagRepository.receiveTagsByGiftCertificateId(updatingGiftCertificate.getId());
            List<Tag> updatingTags = receiveUpdatingTags(new ArrayList<>(targetDto.getTags()), sourceTags);
            updatingGiftCertificate.setTags(new HashSet<>(updatingTags));
        }

        checkIfUpdatingIsPossibleOrThrow(updatingGiftCertificate);

        List<Tag> updatingTags = new ArrayList<>(updatingGiftCertificate.getTags());
        if ((updatingTags != null) && (updatingTags.size() > 0)) { // FIXME delete null
            createIfNotExist(updatingTags);
            giftCertificateRepository.linkGiftCertificateWithTags(targetDto.getId(), updatingTags);
        }

        return crudRepository.update(updatingGiftCertificate);
    }

    /**
     * a method which delete a resource (gift-certificate) of a specific ID: it destructs links between
     * all secondary resources (tags) and the resource and then deletes the resource
     *
     * @param id an identification number of the resource  which should be removed from a data source
     * @return true if the resource is successfully deleted
     */
    @Transactional
    @Override
    public boolean delete(Long id) {
        giftCertificateRepository.deleteLinkBetweenGiftCertificateAndTags(id);
        return super.delete(id);
    }

    @Override
    protected Map<String, Object> receiveUniqueConstraints(GiftCertificateDto certificateDto) {
        return Collections.singletonMap("name", certificateDto.getName());
    }

    @Override
    protected SqlSpecification getSqlSpecification(SearchCriteriaDto<GiftCertificate> criteria) {
        if (criteria == null) {
            return new GiftCertificateSpecification();
        }

        if (criteria.getClass() != GiftCertificateSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
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

    private void createIfNotExist(List<Tag> tags) {
        tags.stream()
                .filter(tag -> !tagRepository.exists(tag.getName()))
                .forEach(tagRepository::create);
    }

    private List<Tag> receiveUpdatingTags(List<TagDto> targetTags, List<Tag> sourceTags) {
        return targetTags.stream()
                .filter(targetTagDto -> sourceTags.stream()
                        .noneMatch(sourceTag -> sourceTag.getName().equals(targetTagDto.getName())))
                .map(tagConverter::convertToDomain)
                .collect(Collectors.toList());
    }
}
