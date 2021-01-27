package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.GeneralEntityConverter;
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
}
