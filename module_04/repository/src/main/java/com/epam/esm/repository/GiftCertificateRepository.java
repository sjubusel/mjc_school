package com.epam.esm.repository;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;

import java.util.Set;

public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {

    void linkGiftCertificateWithTags(Long createdId, Set<Tag> updatingTags);

}
