package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.TagSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import com.epam.esm.service.exception.EmptyUpdateException;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends GeneralCrudService<TagDto, Tag, Long, TagDto> implements TagService {

    private final TagRepository tagRepository;

    @Value("${page.default-page-size}")
    private Integer defaultPageSize;

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
    protected JpaSpecification<Tag, Long> getDataSourceSpecification(SearchCriteriaDto<Tag> searchCriteria) {
        if (searchCriteria == null) {
            return new TagSpecification(null, defaultPageSize, null);
        }

        if (searchCriteria.getClass() != TagSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        TagSearchCriteriaDto params = (TagSearchCriteriaDto) searchCriteria;
        Integer pageSize = params.getPageSize() == null ? defaultPageSize : params.getPageSize();
        return new TagSpecification(params.getName(), pageSize, params.getPage());
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
    protected Map<String, Object> receiveUniqueConstraints(TagDto dto) {
        Map<String, Object> uniqueConstrains = new HashMap<>();
        uniqueConstrains.putIfAbsent("name", dto.getName());
        return uniqueConstrains;
    }
}
