package com.epam.esm.service.old.converter;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.model.dto.EntityDto;

import java.io.Serializable;

public interface EntityConverter<DTO extends EntityDto<ID>, DOMAIN extends GeneralEntity<ID>, ID extends Serializable> {

    DOMAIN convertToDomain(DTO dto);

    DTO convertToDto(DOMAIN domain);

    DOMAIN convertToUpdatingDomain(DOMAIN sourceDomain, DTO targetDto);
}
