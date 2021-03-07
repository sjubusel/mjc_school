package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.model.dto.EntityDto;
import com.epam.esm.repository_new.GeneralCrudRepository;
import com.epam.esm.service.CrudService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.DuplicateResourceException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

public abstract class GeneralCrudService<DTO extends EntityDto<ID, DTO>, DOMAIN extends Entity<ID>,
        ID extends Serializable, UPDATE_DTO extends EntityDto<ID, UPDATE_DTO>>
        implements CrudService<DTO, DOMAIN, ID, UPDATE_DTO> {

    protected final GeneralCrudRepository<DOMAIN, ID> crudRepository;
    protected final GeneralEntityConverter<DTO, DOMAIN, ID> converter;

    @Value("${page.default-page-size}")
    private Integer defaultPageSize;

    protected GeneralCrudService(GeneralCrudRepository<DOMAIN, ID> crudRepository,
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
        Optional<DOMAIN> entity = crudRepository.findById(id);
        DOMAIN domain = entity.orElseThrow(() -> new ResourceNotFoundException(id));
        return converter.convertToDto(domain);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DTO> query(SearchCriteriaDto<DOMAIN> searchCriteria) {
        Page<DOMAIN> domains = crudRepository.findAll(assembleJpaSpecification(searchCriteria),
                assemblePageable(searchCriteria));
        if (domains.isEmpty()) {
            throw new ResourceNotFoundException("Requested resources are not found");
        }
        return domains.map(converter::convertToDto);
    }

    @Transactional
    @Override
    public ID create(DTO dto) {
        if (crudRepository.exists(receiveUniqueConstraints(dto))) {
            throw new DuplicateResourceException(dto.toString());
        }
        DOMAIN entity = converter.convertToDomain(dto);
        return crudRepository.save(entity).getId();
    }

    @Transactional
    @Override
    public DTO update(UPDATE_DTO dto) {
        DOMAIN sourceDomain = receiveDomainWhichIsToBeUpdated(dto.getId());
        DOMAIN targetDomain = receiveUpdatingDomain(sourceDomain, dto);
        return converter.convertToDto(crudRepository.save(targetDomain));
    }

    @Transactional
    @Override
    public boolean delete(ID id) {
        DOMAIN sourceDomain = receiveDomainWhichIsToBeUpdated(id);
        deleteAssociationsWithRelatedEntitiesIfNecessary(sourceDomain);
        return crudRepository.deleteInSoftMode(id);
    }

    protected void deleteAssociationsWithRelatedEntitiesIfNecessary(DOMAIN sourceDomain) {
    }

    protected abstract Specification<DOMAIN> assembleJpaSpecification(SearchCriteriaDto<DOMAIN> searchCriteria);

    protected Pageable assemblePageable(SearchCriteriaDto<DOMAIN> searchCriteria){
        if (searchCriteria == null) {
            return PageRequest.of(0, defaultPageSize);
        }

        Integer page = searchCriteria.getPage();
        Integer pageSize = searchCriteria.getPageSize();

        int actualPage = (page != null) ? (page - 1) : 0;
        int actualPageSize = (pageSize != null) ? pageSize : defaultPageSize;

        return PageRequest.of(actualPage, actualPageSize);
    }

    protected abstract Example<DOMAIN> receiveUniqueConstraints(DTO dto);

    protected DOMAIN receiveDomainWhichIsToBeUpdated(ID id) {
        Optional<DOMAIN> result = crudRepository.findById(id);
        return result.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    protected abstract DOMAIN receiveUpdatingDomain(DOMAIN sourceDomain, UPDATE_DTO dto);

}
