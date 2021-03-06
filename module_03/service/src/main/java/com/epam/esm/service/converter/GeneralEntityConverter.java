package com.epam.esm.service.converter;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.model.dto.EntityDto;

import java.io.Serializable;

public interface GeneralEntityConverter<DTO extends EntityDto<ID, DTO>, DOMAIN extends Entity<ID>,
        ID extends Serializable> {

    DOMAIN convertToDomain(DTO dto);

    DTO convertToDto(DOMAIN domain);
}
