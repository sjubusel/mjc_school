package com.epam.esm.service.validation;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.model.dto.EntityDto;

import java.io.Serializable;

public interface ServiceValidator<DTO extends EntityDto<ID>, DOMAIN extends Entity<ID>, ID extends Serializable> {

    boolean isDtoValidToUpdate(DTO targetDto);

    boolean isDomainValidToUpdate(DOMAIN updatingDomain);
}
