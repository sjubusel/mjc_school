package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.model.dto.GeneralEntityDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.service.CrudService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.DuplicateResourceException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return converter.convertToDto(domain);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DTO> query(SearchCriteriaDto<DOMAIN> searchCriteria) {
        List<DOMAIN> domains = (List<DOMAIN>) crudRepository.query(getDataSourceSpecification(searchCriteria));
        if (domains.isEmpty()) {
            throw new ResourceNotFoundException("Requested resources are not found");
        }
        return domains.stream()
                .map(converter::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ID create(DTO dto) {
        if (crudRepository.exists(receiveUniqueConstraints(dto))) {
            throw new DuplicateResourceException(dto + " already exists"); // fixme add an entity as a parameter
        }
        DOMAIN entity = converter.convertToDomain(dto);
        return crudRepository.create(entity);
    }

    protected abstract Map<String, Object> receiveUniqueConstraints(DTO dto);

    @Transactional
    @Override
    public boolean update(DTO dto) {
        DOMAIN sourceDomain = receiveDomainWhichIsToBeUpdated(dto);
        DOMAIN targetDomain = receiveUpdatingDomain(sourceDomain, dto);
        return crudRepository.update(targetDomain);
    }

    @Override
    public boolean delete(ID id) {
        return false;
    }

    protected abstract JpaSpecification<DOMAIN, ID> getDataSourceSpecification(SearchCriteriaDto<DOMAIN> searchCriteria);

    protected DOMAIN receiveDomainWhichIsToBeUpdated(DTO dto) {
        Optional<DOMAIN> result = crudRepository.findOne(dto.getId());
        return result.orElseThrow(() -> new ResourceNotFoundException(dto.getId()));
    }

    protected abstract DOMAIN receiveUpdatingDomain(DOMAIN sourceDomain, DTO dto);

}
