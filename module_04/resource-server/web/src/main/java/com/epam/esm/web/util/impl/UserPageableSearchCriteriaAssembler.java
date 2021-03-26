package com.epam.esm.web.util.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
import com.epam.esm.web.util.PageableSearchCriteriaAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserPageableSearchCriteriaAssembler extends PageableSearchCriteriaAssembler<User, Long> {

    @Override
    protected SearchCriteriaDto<User> receiveEmptySearchCriteria() {
        return new UserSearchCriteriaDto();
    }
}
