package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.model.dto.SearchCriteriaDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.SqlSpecification;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecification;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.EntityConverter;
import com.epam.esm.service.validation.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftCertificateServiceImpl extends BasicCrudService<GiftCertificateDto, GiftCertificate, Long>
        implements GiftCertificateService {

    @Autowired
    public GiftCertificateServiceImpl(EntityConverter<GiftCertificateDto, GiftCertificate, Long> entityConverter,
                                      CrudRepository<GiftCertificate, Long> crudRepository,
                                      ServiceValidator<GiftCertificateDto, GiftCertificate, Long> validator) {
        super(entityConverter, crudRepository, validator);
    }

    @Override
    protected String getMainUniqueEntityValue(GiftCertificateDto certificateDto) {
        return certificateDto.getName();
    }

    @Override
    protected SqlSpecification getSqlSpecification(SearchCriteriaDto<GiftCertificate> searchCriteria) {
        if (searchCriteria == null) {
            return new GiftCertificateSpecification();
        }
        if (searchCriteria.getClass() != GiftCertificateSearchCriteriaDto.class) {
            throw new RuntimeException(); // FIXME
        }
        return new GiftCertificateSpecification(((GiftCertificateSearchCriteriaDto) searchCriteria));
    }
}
