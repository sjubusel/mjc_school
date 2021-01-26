package com.epam.esm.service.validation;

import com.epam.esm.model.domain.GeneralEntity;

import java.io.Serializable;

public interface ServiceValidator<DOMAIN extends GeneralEntity<ID>, ID extends Serializable> {

    boolean isDomainValidToUpdate(DOMAIN updatingDomain);
}
