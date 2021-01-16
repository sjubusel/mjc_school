package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.model.dto.EntityDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.SqlSpecification;
import com.epam.esm.service.CrudService;
import com.epam.esm.service.converter.EntityConverter;
import com.epam.esm.service.validation.ServiceValidator;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BasicCrudService<DTO extends EntityDto<ID>, DOMAIN extends Entity<ID>, ID extends Serializable>
        implements CrudService<DTO, DOMAIN, ID> {

    protected final EntityConverter<DTO, DOMAIN, ID> entityConverter;
    protected final CrudRepository<DOMAIN, ID> crudRepository;
    protected final ServiceValidator<DTO, DOMAIN, ID> validator;

    public BasicCrudService(EntityConverter<DTO, DOMAIN, ID> entityConverter, CrudRepository<DOMAIN, ID> crudRepository,
                            ServiceValidator<DTO, DOMAIN, ID> validator) {
        this.entityConverter = entityConverter;
        this.crudRepository = crudRepository;
        this.validator = validator;
    }

    /**
     * a method which applied to verify if an entity, which is represented by <code>DTO dto</code>, is already created
     *
     * @param dto an object which contains fields of an entity that should be created
     * @return a String, which unifies
     */
    protected abstract String getMainUniqueEntityValue(DTO dto);

    protected abstract SqlSpecification getSqlSpecification(SearchCriteriaDto<DOMAIN> searchCriteria);

    @Transactional
    @Override
    public ID create(DTO dto) {
        if (crudRepository.exists(getMainUniqueEntityValue(dto))) {
            throw new RuntimeException(); // FIXME
        }
        DOMAIN domain = entityConverter.convertToDomain(dto);
        return crudRepository.create(domain);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DTO> query(SearchCriteriaDto<DOMAIN> searchCriteria) {
        List<DOMAIN> domains = (List<DOMAIN>) crudRepository.query(getSqlSpecification(searchCriteria));
        if (domains.size() == 0) {
            throw new RuntimeException(); // FIXME
        }
        return domains.stream().map(entityConverter::convertToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public DTO findOne(ID id) {
        Optional<DOMAIN> entity = crudRepository.findOne(id);
        return entityConverter.convertToDto(entity.orElseThrow(RuntimeException::new)); // FIXME
    }

    @Transactional
    @Override
    public boolean update(DTO targetDto) {
        DOMAIN updatingDomain = generateUpdatingDomain(targetDto);
        return crudRepository.update(updatingDomain);
    }

    @Transactional
    @Override
    public boolean delete(ID id) {
        Optional<DOMAIN> entity = crudRepository.findOne(id);
        entity.orElseThrow(RuntimeException::new); // FIXME add a custom exception
        return crudRepository.delete(id);
    }


    protected DOMAIN generateUpdatingDomain(DTO targetDto) {
        if (!validator.isDtoValidToUpdate(targetDto)) {
            throw new RuntimeException();
        }

        Optional<DOMAIN> source = crudRepository.findOne(targetDto.getId());
        DOMAIN sourceDomain = source.orElseThrow(RuntimeException::new); // FIXME add a custom exception
        DOMAIN updatingDomain = entityConverter.convertToUpdatingDomain(sourceDomain, targetDto);

        if (!validator.isDomainValidToUpdate(updatingDomain)) {
            throw new RuntimeException(); // FIXME
        }

        return updatingDomain;
    }
}
