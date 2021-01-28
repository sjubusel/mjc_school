package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.TagSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTagService extends GeneralCrudService<TagDto, Tag, Long> implements TagService {

    @Autowired
    protected DefaultTagService(CrudRepository<Tag, Long> crudRepository,
                                GeneralEntityConverter<TagDto, Tag, Long> converter) {
        super(crudRepository, converter);
    }

    @Override
    protected JpaSpecification<Tag, Long> getDataSourceSpecification(SearchCriteriaDto<Tag> searchCriteria) {
        if (searchCriteria == null) {
            return new TagSpecification();
        }

        if (searchCriteria.getClass() != TagSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        TagSearchCriteriaDto params = (TagSearchCriteriaDto) searchCriteria;
        return new TagSpecification(params.getName(), params.getPage());
    }
}
