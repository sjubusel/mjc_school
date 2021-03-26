package com.epam.esm.web.util.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.web.util.PageableSearchCriteriaAssembler;
import org.springframework.stereotype.Component;

@Component
public class OrderPageableSearchCriteriaAssembler extends PageableSearchCriteriaAssembler<Order, Long> {

    @Override
    protected SearchCriteriaDto<Order> receiveEmptySearchCriteria() {
        return new OrderSearchCriteriaDto();
    }
}
