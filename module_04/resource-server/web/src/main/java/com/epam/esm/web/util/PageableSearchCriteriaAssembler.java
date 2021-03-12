package com.epam.esm.web.util;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.service.dto.SearchCriteriaDto;

import java.io.Serializable;

public abstract class PageableSearchCriteriaAssembler<T extends Entity<ID>, ID extends Serializable> {

    public SearchCriteriaDto<T> toSearchCriteria(SearchCriteriaDto<T> searchCriteriaDto, Integer page,
                                                 Integer pageSize) {
        searchCriteriaDto.setPage(page);
        searchCriteriaDto.setPageSize(pageSize);
        return searchCriteriaDto;
    }
}
