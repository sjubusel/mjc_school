package com.epam.esm.service.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.UserSpecification;
import com.epam.esm.service.UserService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultUserService extends GeneralCrudService<UserDto, User, Long, UserDto> implements UserService {

    @Autowired
    protected DefaultUserService(CrudRepository<User, Long> crudRepository,
                                 GeneralEntityConverter<UserDto, User, Long> converter) {
        super(crudRepository, converter);
    }

    @Override
    protected Map<String, Object> receiveUniqueConstraints(UserDto dto) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }

    @Override
    protected void deleteAssociationsWithRelatedEntitiesIfNecessary(User sourceDomain) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }

    @Override
    protected JpaSpecification<User, Long> getDataSourceSpecification(SearchCriteriaDto<User> searchCriteria) {
        if (searchCriteria == null) {
            return new UserSpecification();
        }

        if (searchCriteria.getClass() != UserSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        UserSearchCriteriaDto params = (UserSearchCriteriaDto) searchCriteria;
        return new UserSpecification(params.getPage());
    }

    @Override
    protected User receiveUpdatingDomain(User sourceDomain, UserDto dto) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }
}
