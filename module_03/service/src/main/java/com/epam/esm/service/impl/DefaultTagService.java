package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTagService extends GeneralCrudService<TagDto, Tag, Long> implements TagService {

    @Autowired
    protected DefaultTagService(CrudRepository<Tag, Long> crudRepository,
                                GeneralEntityConverter<TagDto, Tag, Long> converter) {
        super(crudRepository, converter);
    }
}
