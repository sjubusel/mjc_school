package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository_new.impl.TagRepository;
import com.epam.esm.repository_new.specification.TagSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import com.epam.esm.service.exception.EmptyUpdateException;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends GeneralCrudService<TagDto, Tag, Long, TagDto> implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    protected TagServiceImpl(TagRepository tagRepository,
                             GeneralEntityConverter<TagDto, Tag, Long> converter) {
        super(tagRepository, converter);
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDto> receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders() {
        return tagRepository.receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders().stream()
                .map(converter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    protected Specification<Tag> assembleJpaSpecification(SearchCriteriaDto<Tag> searchCriteria) {
        if (searchCriteria == null) {
            return new TagSpecification();
        }
        if (searchCriteria.getClass() != TagSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }
        TagSearchCriteriaDto params = (TagSearchCriteriaDto) searchCriteria;
        return new TagSpecification(params.getName());
    }

    @Override
    protected Tag receiveUpdatingDomain(Tag sourceDomain, TagDto dto) {
        String newName = dto.getName();
        if (newName == null || newName.equals(sourceDomain.getName())) {
            throw new EmptyUpdateException(sourceDomain.toString());
        }
        sourceDomain.setName(newName);
        return sourceDomain;
    }

    @Override
    protected Example<Tag> receiveUniqueConstraints(TagDto dto) {
        Tag probe = converter.convertToDomain(dto);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("giftCertificates")
                .withIgnoreNullValues();
        return Example.of(probe, matcher);
    }
}
