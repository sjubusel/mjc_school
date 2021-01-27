package com.epam.esm.service.old.impl;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.model.dto.GeneralEntityDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.old.specification.SqlSpecification;
import com.epam.esm.service.CrudService;
import com.epam.esm.service.old.converter.EntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.DuplicateResourceException;
import com.epam.esm.service.exception.EmptyUpdateException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.old.validation.ServiceValidator;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * a class which realizes business logic of standard CRUD operations of all resources
 * without linking between main and secondary resources. That linking should be performed
 * by overriding public API of this class if it is necessary
 *
 * @param <DTO>    a data transfer object which represents a resource on :controller layer
 * @param <DOMAIN> an object which represents a resource on :repository layer
 * @param <ID>     an object which represents identification number of a resource
 */
public abstract class BasicCrudService<DTO extends GeneralEntityDto<ID>, DOMAIN extends GeneralEntity<ID>, ID extends Serializable>
        implements CrudService<DTO, DOMAIN, ID> {

    protected final EntityConverter<DTO, DOMAIN, ID> entityConverter;
    protected final CrudRepository<DOMAIN, ID> crudRepository;
    protected final ServiceValidator<DOMAIN, ID> validator;

    public BasicCrudService(EntityConverter<DTO, DOMAIN, ID> entityConverter, CrudRepository<DOMAIN, ID> crudRepository,
                            ServiceValidator<DOMAIN, ID> validator) {
        this.entityConverter = entityConverter;
        this.crudRepository = crudRepository;
        this.validator = validator;
    }

    /**
     * a method which applied to verify if an entity, which is represented by <code>DTO dto</code>, is already created
     *
     * @param dto an object which contains fields of an entity that should be created
     * @return a Map of parameters, which make a resource unique
     */
    protected abstract Map<String, Object> receiveUniqueConstraints(DTO dto);

    protected abstract SqlSpecification getSqlSpecification(SearchCriteriaDto<DOMAIN> searchCriteria);

    /**
     * a standard CREATE operation
     *
     * @param dto an object which is received from a client side and which state is to be saved in the datasource
     * @return an object which identifies a newly created entity in a datasource
     */
    @Transactional
    @Override
    public ID create(DTO dto) {
        if (crudRepository.exists(receiveUniqueConstraints(dto))) {
            throw new DuplicateResourceException("Resource already exists in the system");
        }
        DOMAIN domain = entityConverter.convertToDomain(dto);
        return crudRepository.create(domain);
    }

    /**
     * a standard READ operation of resources corresponding to search criteria
     *
     * @param searchCriteria a search parameters which specify target state of resources that are being searched for
     * @return a collection of resources which correspond to search parameters
     */
    @Transactional(readOnly = true)
    @Override
    public List<DTO> query(SearchCriteriaDto<DOMAIN> searchCriteria) {
        List<DOMAIN> domains = (List<DOMAIN>) crudRepository.query(getSqlSpecification(searchCriteria));
        if (domains.size() == 0) {
            throw new ResourceNotFoundException("Requested resources are not found");
        }
        return domains.stream().map(entityConverter::convertToDto).collect(Collectors.toList());
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
        return entityConverter.convertToDto(entity.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    /**
     * a standard UPDATE operation which finds only modified fields of a resource and updates these fields
     *
     * @param targetDto an object with new state of a resource
     * @return true if state of a resource is updated in datasource
     */
    @Transactional
    @Override
    public boolean update(DTO targetDto) {
        DOMAIN updatingDomain = receiveUpdatingDomain(targetDto);

        checkIfUpdatingIsPossibleOrThrow(updatingDomain);

        return crudRepository.update(updatingDomain);
    }

    /**
     * a standard DELETE operation which removes a resource identified by <code>ID id</code> from a data source
     * @param id an object which identifies a resource
     * @return true if deletion is successfully performed
     */
    @Transactional
    @Override
    public boolean delete(ID id) {
        Optional<DOMAIN> entity = crudRepository.findOne(id);

        entity.orElseThrow(() -> new ResourceNotFoundException(id));

        return crudRepository.delete(id);
    }


    protected DOMAIN receiveUpdatingDomain(DTO targetDto) {
        Optional<DOMAIN> source = crudRepository.findOne(targetDto.getId());
        DOMAIN sourceDomain = source.orElseThrow(() -> new ResourceNotFoundException(targetDto.getId()));

        return entityConverter.convertToUpdatingDomain(sourceDomain, targetDto);
    }

    protected void checkIfUpdatingIsPossibleOrThrow(DOMAIN updatingDomain) {
        if (!validator.isDomainValidToUpdate(updatingDomain)) {
            throw new EmptyUpdateException(String.format("No changes are applied to Resource №%s",
                    updatingDomain.getId()));
        }
    }
}
