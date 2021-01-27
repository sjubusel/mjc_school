package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.model.dto.GeneralEntityDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.service.CrudService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class GeneralCrudService<DTO extends GeneralEntityDto<ID>, DOMAIN extends GeneralEntity<ID>,
        ID extends Serializable> implements CrudService<DTO, DOMAIN, ID> {

    protected final CrudRepository<DOMAIN, ID> crudRepository;
    protected final GeneralEntityConverter<DTO, DOMAIN, ID> converter;

    protected GeneralCrudService(CrudRepository<DOMAIN, ID> crudRepository,
                                 GeneralEntityConverter<DTO, DOMAIN, ID> converter) {
        this.crudRepository = crudRepository;
        this.converter = converter;
    }

    /**
     * a standard READ operation of a separate resource which can be identified by <code>ID id</code>
     *
     * @param id an object which identifies a resource which is being searched for
     * @return an object which represents a resource and which is to be shown on a client side
     */
    @Transactional(readOnly = true)
    @Override
    public DTO findOne(ID id) {
        Optional<DOMAIN> entity = crudRepository.findOne(id);
        DOMAIN domain = entity.orElseThrow(() -> new ResourceNotFoundException(id));
        DTO dto = converter.convertToDto(domain);
        System.out.println(dto); // fixme
        return dto;
    }

    @Override
    public ID create(DTO entity) {
        return null;
    }

    @Override
    public List<DTO> query(SearchCriteriaDto<DOMAIN> searchParams) {
        return null;
    }

    @Override
    public boolean update(DTO entity) {
        return false;
    }

    @Override
    public boolean delete(ID id) {
        return false;
    }
}
