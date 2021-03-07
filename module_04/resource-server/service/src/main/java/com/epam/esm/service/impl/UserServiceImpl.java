package com.epam.esm.service.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.repository_new.GeneralCrudRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GeneralCrudService<UserDto, User, Long, UserDto> implements UserService {

    @Autowired
    protected UserServiceImpl(GeneralCrudRepository<User, Long> crudRepository,
                              GeneralEntityConverter<UserDto, User, Long> converter) {
        super(crudRepository, converter);
    }

    @Override
    protected Example<User> receiveUniqueConstraints(UserDto dto) {
//        Map<String, Object> uniqueConstrains = new HashMap<>();
//        uniqueConstrains.put("login", dto.getLogin());
//        uniqueConstrains.put("phoneNumber", dto.getPhoneNumber());
//        uniqueConstrains.put("email", dto.getEmail());
//        return uniqueConstrains;
        return null;
    }

    @Override
    protected void deleteAssociationsWithRelatedEntitiesIfNecessary(User sourceDomain) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }

    @Override
    protected Specification<User> assembleJpaSpecification(SearchCriteriaDto<User> searchCriteria) {
//        if (searchCriteria == null) {
//            return new UserSpecification(null, defaultPageSize);
//        }
//
//        if (searchCriteria.getClass() != UserSearchCriteriaDto.class) {
//            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
//        }
//
//        UserSearchCriteriaDto params = (UserSearchCriteriaDto) searchCriteria;
//        Integer pageSize = params.getPageSize() == null ? defaultPageSize : params.getPageSize();
//        return new UserSpecification(params.getPage(), pageSize);
        return null;
    }

    @Override
    protected User receiveUpdatingDomain(User sourceDomain, UserDto dto) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }
}
