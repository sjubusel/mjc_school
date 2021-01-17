package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import com.epam.esm.repository.specification.SqlSpecification;
import com.epam.esm.repository.specification.impl.TagSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.EntityConverter;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import com.epam.esm.service.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagServiceImpl extends BasicCrudService<TagDto, Tag, Long> implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(EntityConverter<TagDto, Tag, Long> entityConverter, TagRepository tagRepository,
                          ServiceValidator<Tag, Long> validator) {
        super(entityConverter, tagRepository, validator);
        this.tagRepository = tagRepository;
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        tagRepository.deleteLinkBetweenTagAndGiftCertificates(id);
        return super.delete(id);
    }

    @Override
    protected String getMainUniqueEntityValue(TagDto tagDto) {
        return tagDto.getName();
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
