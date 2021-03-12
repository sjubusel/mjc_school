package com.epam.esm.web.util.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.web.util.PageableSearchCriteriaAssembler;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificatePageableSearchCriteriaAssembler
        extends PageableSearchCriteriaAssembler<GiftCertificate, Long> {

    @Override
    protected SearchCriteriaDto<GiftCertificate> receiveEmptySearchCriteria() {
        return new GiftCertificateSearchCriteriaDto();
    }
}
