package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Order;
import com.epam.esm.model.domain.OrderPosition;
import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderPositionRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.OrderSpecification;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.IllegalRequestException;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import com.epam.esm.service.exception.InconsistentCreateDtoException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends GeneralCrudService<OrderDto, Order, Long, OrderDto> implements OrderService {

    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderPositionRepository orderPositionRepository;

    @Value("${page.default-page-size}")
    private Integer defaultPageSize;

    @Autowired
    protected OrderServiceImpl(CrudRepository<Order, Long> crudRepository,
                               GeneralEntityConverter<OrderDto, Order, Long> converter,
                               UserRepository userRepository,
                               GiftCertificateRepository giftCertificateRepository,
                               OrderPositionRepository orderPositionRepository) {
        super(crudRepository, converter);
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.orderPositionRepository = orderPositionRepository;
    }

    @Transactional
    @Override
    public Long create(OrderDto dto) {
        checkIfOrderConsistentOtherwiseThrow(dto);

        User userToLink = receiveUserIfExistsOtherwiseThrow(dto);
        Order orderToCreate = receiveOrderToCreate(userToLink);
        Long createdId = crudRepository.create(orderToCreate);
        orderToCreate.setId(createdId);

        Set<OrderPosition> orderPositionsToCreate = receiveOrderPositionsIfAllGiftCertificatesExist(dto, orderToCreate);
        Set<OrderPosition> createdOrderPositions = orderPositionRepository.createOrderPositions(orderPositionsToCreate);
        orderToCreate.setOrderPositions(createdOrderPositions);
        crudRepository.update(orderToCreate);

        return createdId;
    }

    @Override
    protected Map<String, Object> receiveUniqueConstraints(OrderDto dto) {
        return EMPTY_UNIQUE_CONSTRAINTS;
    }

    @Override
    protected JpaSpecification<Order, Long> getDataSourceSpecification(SearchCriteriaDto<Order> searchCriteria) {
        if (searchCriteria == null) {
            return new OrderSpecification(null, defaultPageSize, null);
        }

        if (searchCriteria.getClass() != OrderSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        OrderSearchCriteriaDto params = (OrderSearchCriteriaDto) searchCriteria;
        Integer pageSize = params.getPageSize() == null ? defaultPageSize : params.getPageSize();
        return new OrderSpecification(params.getPage(), pageSize, params.getUserId());
    }

    @Override
    protected Order receiveUpdatingDomain(Order sourceDomain, OrderDto dto) {
        return null;
    }

    private void checkIfOrderConsistentOtherwiseThrow(OrderDto dto) {
        Long userId = dto.getUser().getId();
        if (userId == null) {
            throw new InconsistentCreateDtoException(dto.toString());
        }

        if (dto.getOrderPositions().stream()
                .anyMatch(orderPositionDto -> orderPositionDto.getGiftCertificate().getId() == null)) {
            throw new InconsistentCreateDtoException(dto.toString());
        }
    }

    private User receiveUserIfExistsOtherwiseThrow(OrderDto dto) {
        User userToLink;
        Optional<User> user = userRepository.findOne(dto.getUser().getId());
        userToLink = user.orElseThrow(() -> new ResourceNotFoundException(dto.getUser().getId())); // fixme
        return userToLink;
    }

    private Order receiveOrderToCreate(User userToLink) {
        return new Order(Instant.now(), userToLink, null);
    }

    private Set<OrderPosition> receiveOrderPositionsIfAllGiftCertificatesExist(OrderDto dto, Order orderToCreate) {
        return dto.getOrderPositions().stream()
                .map(orderPosition -> {
                    Long certificateId = orderPosition.getGiftCertificate().getId();
                    Optional<GiftCertificate> certificate = giftCertificateRepository.findOne(certificateId);
                    return certificate.orElseThrow(() -> new ResourceNotFoundException(certificateId));
                })
                .map(giftCertificate -> new OrderPosition(giftCertificate.getPrice(), orderToCreate, giftCertificate))
                .collect(Collectors.toSet());
    }

    @Override
    public OrderDto findOrderByIdIfBelongsToUser(Long orderId, Long userId) {
        User user = userRepository.findOne(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
        Order order = crudRepository.findOne(orderId).orElseThrow(() -> new ResourceNotFoundException(userId));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalRequestException();
        }
        return converter.convertToDto(order);
    }
}
