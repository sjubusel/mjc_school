package com.epam.esm.service.old.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.old.specification.SqlSpecification;
import com.epam.esm.repository.old.specification.impl.TagSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.service.old.converter.EntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import com.epam.esm.service.old.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

/**
 * a class which realizes business logic of CRUD operations applied to resources called "Tags"
 */
@Service
public class TagServiceImpl extends BasicCrudService<TagDto, Tag, Long> implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(EntityConverter<TagDto, Tag, Long> entityConverter, TagRepository tagRepository,
                          ServiceValidator<Tag, Long> validator) {
        super(entityConverter, tagRepository, validator);
        this.tagRepository = tagRepository;
    }

    /**
     * a method which deletes links between a target resource (tag) and secondary related resources (gift-certificates)
     * and then deletes the resource from a datasource
     * @param id an identification number of the resource that is to be deleted
     * @return true if the resource is successfully deleted
     */
    @Transactional
    @Override
    public boolean delete(Long id) {
        tagRepository.deleteLinkBetweenTagAndGiftCertificates(id);
        return super.delete(id);
    }

    @Override
    protected Map<String, Object> receiveUniqueConstraints(TagDto tagDto) {
        return Collections.singletonMap("name", tagDto.getName());
    }

    @Override
    protected SqlSpecification getSqlSpecification(SearchCriteriaDto<Tag> criteria) {
        if (criteria == null) {
            return new TagSpecification();
        }

        if (criteria.getClass() != TagSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        TagSearchCriteriaDto searchCriteria = (TagSearchCriteriaDto) criteria;
        return new TagSpecification(searchCriteria.getName());
    }
}
