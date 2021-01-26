package com.epam.esm.service;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.model.dto.EntityDto;
import com.epam.esm.service.dto.SearchCriteriaDto;

import java.io.Serializable;
import java.util.List;

public interface CrudService<DTO extends EntityDto<ID>, DOMAIN extends GeneralEntity<ID>, ID extends Serializable> {

    ID create(DTO entity);

    List<DTO> query(SearchCriteriaDto<DOMAIN> searchParams);

    DTO findOne(ID id);

    boolean update(DTO entity);

    boolean delete(ID id);
}
