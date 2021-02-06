package com.epam.esm.service.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.UserSpecification;
import com.epam.esm.service.converter.impl.UserConverterImpl;
import com.epam.esm.service.dto.UserSearchCriteriaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, new UserConverterImpl());
    }

    @Test
    void receiveUniqueConstraints() {
        UserDto userDto = new UserDto("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");

        assertThrows(RuntimeException.class, () -> userService.receiveUniqueConstraints(userDto));
    }

    @Test
    void deleteAssociationsWithRelatedEntitiesIfNecessary() {
        assertThrows(RuntimeException.class,
                () -> userService.deleteAssociationsWithRelatedEntitiesIfNecessary(new User()));
    }

    @DisplayName("the most common path of getDataSourceSpecification() execution")
    @Test
    void getDataSourceSpecificationWithMostCommonPath() {
        UserSearchCriteriaDto criteriaDto = new UserSearchCriteriaDto(12);
        UserSpecification expected = new UserSpecification(criteriaDto.getPage());

        JpaSpecification<User, Long> actual = userService.getDataSourceSpecification(criteriaDto);
        assertEquals(expected, actual);
    }

    @DisplayName("the least common path of getDataSourceSpecification() execution")
    @Test
    void getDataSourceSpecificationWithLeastCommonPath() {
        UserSpecification expected = new UserSpecification();

        JpaSpecification<User, Long> actual = userService.getDataSourceSpecification(null);
        assertEquals(expected, actual);
    }

    @Test
    void receiveUpdatingDomain() {
        UserDto userDto = new UserDto("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");

        assertThrows(RuntimeException.class, () -> userService.receiveUpdatingDomain(new User(), userDto));
    }

    @Test
    void findOne() {
        Long id = 1L;
        User user = new User("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");
        user.setId(id);
        UserDto expected = new UserDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber());
        expected.setId(id);

        when(userRepository.findOne(id)).thenReturn(Optional.of(user));

        UserDto actual = userService.findOne(id);
        assertEquals(expected, actual);
    }

    @Test
    void query() {
        UserSearchCriteriaDto criteriaDto = new UserSearchCriteriaDto(1);
        UserSpecification specification = new UserSpecification(criteriaDto.getPage());
        Long id = 1L;
        User user = new User("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");
        user.setId(id);
        UserDto expectedDto = new UserDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber());
        expectedDto.setId(id);
        List<UserDto> expected = Collections.singletonList(expectedDto);

        when(userRepository.query(specification)).thenReturn(Collections.singletonList(user));

        List<UserDto> actual = userService.query(criteriaDto);
        assertEquals(expected, actual);
    }

    @Test
    void create() {
        UserDto userDto = new UserDto("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");

        Map<String, Object> map = new HashMap<>();
        map.putIfAbsent("first_name", userDto.getFirstName());
        map.putIfAbsent("last_name", userDto.getLastName());
        map.putIfAbsent("email", userDto.getEmail());
        map.putIfAbsent("phone_number", userDto.getPhoneNumber());

        when(userRepository.exists(map)).thenReturn(Boolean.FALSE);

        assertThrows(RuntimeException.class, () -> userService.create(userDto));
    }

    @Test
    void update() {
        Long id = 1L;
        UserDto userDto = new UserDto("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");
        userDto.setId(id);
        User user = new User("Sju", "Busel", "sjubusel@email.com", "+ 380 (29) 111-78-44");
        user.setId(id);

        when(userRepository.findOne(id)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.update(userDto));
    }

    @Test
    void delete() {
        Long id = 1L;
        User user = new User("Sju", "Busel", "sjubusel@email.com", "+ 380 (29) 111-78-44");
        user.setId(id);

        when(userRepository.findOne(id)).thenReturn(Optional.of(user));
        when(userRepository.delete(id)).thenReturn(Boolean.TRUE);
        assertThrows(RuntimeException.class, () -> userService.delete(id));
    }

    @Test
    void receiveDomainWhichIsToBeUpdated() {
        Long id = 1L;
        User expected = new User("Sju", "Busel", "sjubusel@email.com", "+ 380 (29) 111-78-44");

        when(userRepository.findOne(id)).thenReturn(Optional
                .of(new User("Sju", "Busel", "sjubusel@email.com", "+ 380 (29) 111-78-44")));

        User actual = userService.receiveDomainWhichIsToBeUpdated(id);
        assertEquals(expected, actual);
    }
}