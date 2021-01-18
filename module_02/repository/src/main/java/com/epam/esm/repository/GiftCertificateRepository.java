package com.epam.esm.repository;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;

import java.util.List;

public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {

    void linkGiftCertificateWithTags(Long createdId, List<Tag> updatingTags);

    void deleteLinkBetweenGiftCertificateAndTags(Long certificateId);
}
