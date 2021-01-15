package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.SearchCriteriaDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.TagSearchCriteriaDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.SqlSpecification;
import com.epam.esm.repository.specification.impl.TagSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.EntityConverter;
import com.epam.esm.service.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends BasicCrudService<TagDto, Tag, Long> implements TagService {

    @Autowired
    public TagServiceImpl(EntityConverter<TagDto, Tag, Long> entityConverter, CrudRepository<Tag, Long> crudRepository,
                          ServiceValidator<TagDto, Tag, Long> validator) {
        super(entityConverter, crudRepository, validator);
    }

    @Override
    protected String getMainUniqueEntityValue(TagDto tagDto) {
        return tagDto.getName();
    }

    @Override
    protected SqlSpecification getSqlSpecification(SearchCriteriaDto<Tag> searchCriteria) {
        if (searchCriteria == null) {
            return new TagSpecification();
        }

        if (searchCriteria.getClass() != TagSearchCriteriaDto.class) {
            throw new RuntimeException(); // FIXME
        }

        return new TagSpecification(((TagSearchCriteriaDto) searchCriteria));
    }
}
