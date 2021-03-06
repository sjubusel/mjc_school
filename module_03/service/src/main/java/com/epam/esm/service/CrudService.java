package com.epam.esm.service;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.model.dto.EntityDto;
import com.epam.esm.service.dto.SearchCriteriaDto;

import java.io.Serializable;
import java.util.List;

public interface CrudService<DTO extends EntityDto<ID, DTO>, DOMAIN extends Entity<ID>,
        ID extends Serializable, UPDATE_DTO extends EntityDto<ID, UPDATE_DTO>> {

    ID create(DTO entity);

    List<DTO> query(SearchCriteriaDto<DOMAIN> searchParams);

    DTO findOne(ID id);

    boolean update(UPDATE_DTO entity);

    boolean delete(ID id);
}
