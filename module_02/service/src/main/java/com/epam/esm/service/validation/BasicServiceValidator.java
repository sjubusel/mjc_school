package com.epam.esm.service.validation;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.model.dto.EntityDto;

import java.io.Serializable;

public abstract class BasicServiceValidator<DTO extends EntityDto<ID>, DOMAIN extends Entity<ID>,
        ID extends Serializable> implements ServiceValidator<DTO, DOMAIN, ID> {

    @Override
    public boolean isDtoValidToUpdate(DTO targetDto) {
        return (targetDto != null) && (targetDto.getId() != null);
    }
}
