package com.epam.esm.service.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.domain.UserAuthority;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.other.Role;
import com.epam.esm.repository.GeneralCrudRepository;
import com.epam.esm.repository.impl.UserAuthorityRepository;
import com.epam.esm.repository.specification.UserSpecification;
import com.epam.esm.service.UserService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GeneralCrudService<UserDto, User, Long, UserDto> implements UserService {

    private final UserAuthorityRepository userAuthorityRepository;

    @Autowired
    protected UserServiceImpl(GeneralCrudRepository<User, Long> crudRepository,
                              GeneralEntityConverter<UserDto, User, Long> converter,
                              UserAuthorityRepository userAuthorityRepository) {
        super(crudRepository, converter);
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Override
    public Long create(UserDto dto) {
        Long createdId = super.create(dto);
        User user = receiveDomainWhichIsToBeUpdated(createdId);

        UserAuthority defaultUserAuthority = new UserAuthority(Role.USER, user);
        userAuthorityRepository.save(defaultUserAuthority);

        return createdId;
    }

    @Override
    protected Example<User> receiveUniqueConstraints(UserDto dto) {
        User probe = converter.convertToDomain(dto);
        ExampleMatcher orConditionMatcher = ExampleMatcher.matchingAny()
                .withIgnorePaths("firstName", "lastName", "password", "authorities", "id");
        return Example.of(probe, orConditionMatcher);
    }

    @Override
    protected void deleteAssociationsWithRelatedEntitiesIfNecessary(User sourceDomain) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }

    @Override
    protected Specification<User> assembleJpaSpecification(SearchCriteriaDto<User> searchCriteria) {
        if (searchCriteria == null) {
            return new UserSpecification();
        }

        if (searchCriteria.getClass() != UserSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        return new UserSpecification();
    }

    @Override
    protected User receiveUpdatingDomain(User sourceDomain, UserDto dto) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
    }

    @Override
    protected void processDuplicateIfNecessary(UserDto dto) {
        dto.setPassword(null);
    }
}
