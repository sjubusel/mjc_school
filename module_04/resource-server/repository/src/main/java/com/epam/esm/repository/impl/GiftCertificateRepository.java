package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.repository.GeneralCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GiftCertificateRepository extends GeneralCrudRepository<GiftCertificate, Long> {

    @Override
    @Modifying
    @Query("UPDATE #{#entityName} AS e SET e.isDeleted=true, e.deleteDate=CURRENT_TIMESTAMP WHERE e.id=?1")
    void deleteById(Long id);
}
