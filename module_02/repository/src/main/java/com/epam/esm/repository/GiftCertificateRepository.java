package com.epam.esm.repository;

import com.epam.esm.model.domain.GiftCertificate;

public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {

    void linkCertificateWithTag(Long certificateId, String tagId);

    void deleteLinkBetweenGiftCertificateAndTags(Long certificateId);
}
