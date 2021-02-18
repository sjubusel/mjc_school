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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends GeneralCrudService<UserDto, User, Long, UserDto> implements UserService {

    @Value("${page.default-page-size}")
    private Integer defaultPageSize;

    @Autowired
    protected UserServiceImpl(CrudRepository<User, Long> crudRepository,
                              GeneralEntityConverter<UserDto, User, Long> converter) {
        super(crudRepository, converter);
    }

    @Override
    protected Map<String, Object> receiveUniqueConstraints(UserDto dto) {
        Map<String, Object> uniqueConstrains = new HashMap<>();
        uniqueConstrains.put("login", dto.getLogin());
        uniqueConstrains.put("phoneNumber", dto.getPhoneNumber());
        uniqueConstrains.put("email", dto.getEmail());
        return uniqueConstrains;
    }

    @Override
    protected void deleteAssociationsWithRelatedEntitiesIfNecessary(User sourceDomain) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }

    @Override
    protected JpaSpecification<User, Long> getDataSourceSpecification(SearchCriteriaDto<User> searchCriteria) {
        if (searchCriteria == null) {
            return new UserSpecification(null, defaultPageSize);
        }

        if (searchCriteria.getClass() != UserSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        UserSearchCriteriaDto params = (UserSearchCriteriaDto) searchCriteria;
        Integer pageSize = params.getPageSize() == null ? defaultPageSize : params.getPageSize();
        return new UserSpecification(params.getPage(), pageSize);
    }

    @Override
    protected User receiveUpdatingDomain(User sourceDomain, UserDto dto) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }
}
