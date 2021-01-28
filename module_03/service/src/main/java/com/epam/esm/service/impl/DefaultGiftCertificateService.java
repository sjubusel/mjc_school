package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecification;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultGiftCertificateService extends GeneralCrudService<GiftCertificateDto, GiftCertificate, Long>
        implements GiftCertificateService {

    @Autowired
    protected DefaultGiftCertificateService(
            CrudRepository<GiftCertificate, Long> crudRepository,
            GeneralEntityConverter<GiftCertificateDto, GiftCertificate, Long> converter
    ) {
        super(crudRepository, converter);
    }

    @Override
    protected JpaSpecification<GiftCertificate, Long> getDataSourceSpecification(SearchCriteriaDto<GiftCertificate> searchCriteria) {
        if (searchCriteria == null) {
            return new GiftCertificateSpecification();
        }

        if (searchCriteria.getClass() != GiftCertificateSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        GiftCertificateSearchCriteriaDto params = (GiftCertificateSearchCriteriaDto) searchCriteria;
        return new GiftCertificateSpecification(params.getTags(), params.getNamePart(), params.getDescriptionPart(),
                params.getSortParams(), params.getPage());
    }
}