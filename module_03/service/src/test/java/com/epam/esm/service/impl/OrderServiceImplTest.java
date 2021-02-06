package com.epam.esm.service.impl;

import com.epam.esm.model.domain.*;
import com.epam.esm.model.dto.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderPositionRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.OrderSpecification;
import com.epam.esm.service.converter.impl.*;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private OrderPositionRepository orderPositionRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        GiftCertificateConverterImpl giftCertificateConverter = new GiftCertificateConverterImpl(new TagConverterImpl());
        OrderPositionConverterImpl orderPositionConverter = new OrderPositionConverterImpl(giftCertificateConverter);
        OrderConverterImpl orderConverter = new OrderConverterImpl(orderPositionConverter, new UserConverterImpl());
        orderService = new OrderServiceImpl(orderRepository, orderConverter, userRepository, giftCertificateRepository,
                orderPositionRepository);
    }

    @Test
    void findOne() {
        Long id = 1L;
        Order order = receiveSourceOrder(id);
        OrderDto expected = receiveSourceOrderDto(id);
        when(orderRepository.findOne(id)).thenReturn(Optional.of(order));

        OrderDto actual = orderService.findOne(id);
        assertEquals(expected, actual);
    }

    @Test
    void query() {
        Long id = 1L;
        Order result = receiveSourceOrder(id);
        List<OrderDto> expected = Collections.singletonList(receiveSourceOrderDto(id));

        when(orderRepository.query(any(OrderSpecification.class))).thenReturn(Collections.singletonList(result));

        List<OrderDto> actual = orderService.query(new OrderSearchCriteriaDto(12, 999L));
        assertEquals(expected, actual);
    }

    @Test
    void create() {
        Long expectedId = 1L;
        UserDto user = new UserDto();
        user.setId(999L);

        GiftCertificateDto giftCertificateDto1 = new GiftCertificateDto();
        giftCertificateDto1.setId(1L);
        GiftCertificateDto giftCertificateDto2 = new GiftCertificateDto();
        giftCertificateDto2.setId(2L);

        OrderPositionDto orderPosition1 = new OrderPositionDto(null, null, giftCertificateDto1);
        OrderPositionDto orderPosition2 = new OrderPositionDto(null, null, giftCertificateDto2);
        Set<OrderPositionDto> orderPositions = new HashSet<>(Arrays.asList(orderPosition1, orderPosition2));

        OrderDto dto = new OrderDto(null, null, user, orderPositions);

        User expectedUser = new User("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");
        expectedUser.setId(user.getId());

        when(userRepository.findOne(999L)).thenReturn(Optional.of(expectedUser));
        when(orderRepository.create(any(Order.class))).thenReturn(1L);
        when(giftCertificateRepository.findOne(1L)).thenReturn(Optional.of(new GiftCertificate()));
        when(giftCertificateRepository.findOne(2L)).thenReturn(Optional.of(new GiftCertificate()));
        when(orderPositionRepository.createOrderPositions(any(Set.class))).thenReturn(new HashSet<OrderPosition>());
        when(orderRepository.update(any(Order.class))).thenReturn(Boolean.TRUE);

        Long actualId = orderService.create(dto);
        assertEquals(expectedId, actualId);
    }

    @Test
    void update() {
        OrderDto dto = new OrderDto();
        dto.setId(1L);

        when(orderRepository.findOne(1L)).thenReturn(Optional.of(new Order()));
        when(orderRepository.update(null)).thenReturn(Boolean.TRUE);

        boolean isUpdated = orderService.update(dto);
        assertTrue(isUpdated);
    }

    @Test
    void delete() {
        Long id = 1L;

        Order order = receiveSourceOrder(id);

        when(orderRepository.findOne(1L)).thenReturn(Optional.of(order));
        when(orderRepository.delete(1L)).thenReturn(Boolean.TRUE);

        boolean isDeleted = orderService.delete(id);
        assertTrue(isDeleted);
    }

    @Test
    void receiveDomainWhichIsToBeUpdated() {
        assertThrows(ResourceNotFoundException.class, () -> orderService.receiveDomainWhichIsToBeUpdated(1L));
    }

    @Test
    void receiveUniqueConstraints() {
        Map<String, Object> expected = new HashMap<>();

        Map<String, Object> actual = orderService.receiveUniqueConstraints(new OrderDto());

        assertEquals(expected, actual);
    }

    @Test
    void getDataSourceSpecification() {
        OrderSearchCriteriaDto searchCriteria = new OrderSearchCriteriaDto(1, 1L);
        OrderSpecification expected = new OrderSpecification(searchCriteria.getPage(), searchCriteria.getUserId());

        JpaSpecification<Order, Long> actual = orderService.getDataSourceSpecification(searchCriteria);
        assertEquals(expected, actual);
    }

    @Test
    void receiveUpdatingDomain() {
        Order actual = orderService.receiveUpdatingDomain(new Order(), new OrderDto());
        assertNull(actual);
    }

    @Test
    void findOrderByIdIfBelongsToUser() {
        Long orderId = 1L;
        Long userId = 999L;

        User user = new User("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");
        user.setId(999L);
        Order sourceOrder = receiveSourceOrder(orderId);
        OrderDto expected = receiveSourceOrderDto(orderId);

        when(userRepository.findOne(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findOne(orderId)).thenReturn(Optional.of(sourceOrder));

        OrderDto actual = orderService.findOrderByIdIfBelongsToUser(orderId, userId);
        assertEquals(expected, actual);
    }

    private Order receiveSourceOrder(Long id) {
        Instant createDateTime1 = Instant.parse("2020-01-15T01:01:01.465Z");
        Instant updateDateTime1 = Instant.parse("2020-03-23T18:09:56.928Z");
        Instant createDateTime2 = Instant.parse("2019-01-12T06:42:01.456Z");
        Instant updateDateTime2 = Instant.parse("2020-01-15T01:01:01.101Z");
        Instant orderInstant = Instant.parse("2021-02-06T00:00:00.000Z");
        Instant deleteTagInstant = Instant.parse("2020-01-16T01:01:01.000Z");

        User user = new User("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");
        user.setId(999L);
        Order order = new Order(orderInstant, user, null);
        order.setId(id);

        Tag sharedTag = new Tag("shared-tag", null, Boolean.TRUE, deleteTagInstant);
        sharedTag.setId(11111111L);
        Tag tagForGc1 = new Tag("tag-gc-1", null, Boolean.FALSE, null);
        tagForGc1.setId(454654654L);
        Tag tagForGc2 = new Tag("tag-gc-2", null, Boolean.FALSE, null);
        tagForGc1.setId(999999999L);
        Set<Tag> tagsGc1 = new HashSet<>(Arrays.asList(sharedTag, tagForGc1));
        Set<Tag> tagsGc2 = new HashSet<>(Arrays.asList(sharedTag, tagForGc2));
        GiftCertificate giftCertificate1 = new GiftCertificate("test-gc-name-1", "test-gc-description-1",
                new BigDecimal("1.1"), 10, createDateTime1, updateDateTime1, tagsGc1, Boolean.FALSE, null);
        giftCertificate1.setId(79845L);
        GiftCertificate giftCertificate2 = new GiftCertificate("test-gc-name-2", "test-gc-description-2",
                new BigDecimal("6.19"), 15, createDateTime2, updateDateTime2, tagsGc2, Boolean.FALSE, null);
        giftCertificate2.setId(87000L);

        OrderPosition orderPosition1 = new OrderPosition(giftCertificate1.getPrice(), order, giftCertificate1);
        OrderPosition orderPosition2 = new OrderPosition(giftCertificate2.getPrice(), order, giftCertificate2);
        Set<OrderPosition> orderPositions = new HashSet<>(Arrays.asList(orderPosition1, orderPosition2));
        order.setOrderPositions(orderPositions);
        return order;
    }

    private OrderDto receiveSourceOrderDto(Long id) {
        Instant createDateTime1 = Instant.parse("2020-01-15T01:01:01.465Z");
        Instant updateDateTime1 = Instant.parse("2020-03-23T18:09:56.928Z");
        Instant createDateTime2 = Instant.parse("2019-01-12T06:42:01.456Z");
        Instant updateDateTime2 = Instant.parse("2020-01-15T01:01:01.101Z");
        Instant orderInstant = Instant.parse("2021-02-06T00:00:00.000Z");
        Instant deleteTagInstant = Instant.parse("2020-01-16T01:01:01.000Z");

        UserDto userDto = new UserDto("Sju", "Busel", "sjubusel@test.com", "+ 380 (29) 111-78-44");
        userDto.setId(999L);
        OrderDto orderDto = new OrderDto(new BigDecimal("7.29"), orderInstant, userDto, null);
        orderDto.setId(id);

        TagDto sharedTag = new TagDto("shared-tag", null, Boolean.TRUE, deleteTagInstant);
        sharedTag.setId(11111111L);
        TagDto tagForGc1 = new TagDto("tag-gc-1", null, Boolean.FALSE, null);
        tagForGc1.setId(454654654L);
        TagDto tagForGc2 = new TagDto("tag-gc-2", null, Boolean.FALSE, null);
        tagForGc1.setId(999999999L);
        Set<TagDto> tagsGc1 = new HashSet<>(Arrays.asList(sharedTag, tagForGc1));
        Set<TagDto> tagsGc2 = new HashSet<>(Arrays.asList(sharedTag, tagForGc2));
        GiftCertificateDto giftCertificate1 = new GiftCertificateDto("test-gc-name-1", "test-gc-description-1",
                new BigDecimal("1.1"), 10, createDateTime1, updateDateTime1, tagsGc1, Boolean.FALSE, null);
        giftCertificate1.setId(79845L);
        GiftCertificateDto giftCertificate2 = new GiftCertificateDto("test-gc-name-2", "test-gc-description-2",
                new BigDecimal("6.19"), 15, createDateTime2, updateDateTime2, tagsGc2, Boolean.FALSE, null);
        giftCertificate2.setId(87000L);

        OrderPositionDto orderPosition1 = new OrderPositionDto(giftCertificate1.getPrice(), null, giftCertificate1);
        OrderPositionDto orderPosition2 = new OrderPositionDto(giftCertificate2.getPrice(), null, giftCertificate2);
        Set<OrderPositionDto> orderPositions = new HashSet<>(Arrays.asList(orderPosition1, orderPosition2));
        orderDto.setOrderPositions(orderPositions);

        return orderDto;
    }
}