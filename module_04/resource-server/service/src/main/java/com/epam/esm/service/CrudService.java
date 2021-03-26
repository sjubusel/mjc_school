package com.epam.esm.service;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.model.dto.EntityDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public interface CrudService<DTO extends EntityDto<ID, DTO>, DOMAIN extends Entity<ID>,
        ID extends Serializable, UPDATE_DTO extends EntityDto<ID, UPDATE_DTO>> {

    ID create(DTO entity);

    Page<DTO> query(SearchCriteriaDto<DOMAIN> searchParams);

    DTO findOne(ID id);

    DTO update(UPDATE_DTO entity);

    boolean delete(ID id);
}
