package com.epam.esm.repository;

import com.epam.esm.model.domain.Tag;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> receiveTagsByGiftCertificateId(Long id);

    void deleteLinkBetweenTagAndGiftCertificates(Long id);

    void createIfNotExist(List<Tag> tags);
}
