package com.epam.esm.web.util.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import com.epam.esm.web.util.PageableSearchCriteriaAssembler;
import org.springframework.stereotype.Component;

@Component
public class TagPageableSearchCriteriaAssembler extends PageableSearchCriteriaAssembler<Tag, Long> {

    @Override
    protected SearchCriteriaDto<Tag> receiveEmptySearchCriteria() {
        return new TagSearchCriteriaDto();
    }
}
