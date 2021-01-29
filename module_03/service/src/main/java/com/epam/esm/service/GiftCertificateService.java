package com.epam.esm.service;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.GiftCertificateUpdateDto;

public interface GiftCertificateService extends CrudService<GiftCertificateDto, GiftCertificate, Long,
        GiftCertificateUpdateDto> {

}
