package com.epam.esm.service.validation;

import com.epam.esm.model.domain.Entity;

import java.io.Serializable;

public interface ServiceValidator<DOMAIN extends Entity<ID>, ID extends Serializable> {

    boolean isDomainValidToUpdate(DOMAIN updatingDomain);
}
