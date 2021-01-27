package com.epam.esm.service.converter;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.model.dto.GeneralEntityDto;

import java.io.Serializable;

public interface GeneralEntityConverter<DTO extends GeneralEntityDto<ID>, DOMAIN extends GeneralEntity<ID>,
        ID extends Serializable> {

    DOMAIN convertToDomain(DTO dto);

    DTO convertToDto(DOMAIN domain);
}
