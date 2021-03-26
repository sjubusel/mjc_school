package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Order;
import com.epam.esm.model.domain.OrderPosition;
import com.epam.esm.model.domain.User;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.repository.impl.GiftCertificateRepository;
import com.epam.esm.repository.impl.OrderPositionRepository;
import com.epam.esm.repository.impl.UserRepository;
import com.epam.esm.repository.GeneralCrudRepository;
import com.epam.esm.repository.specification.OrderSpecification;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.IllegalRequestException;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
import com.epam.esm.service.exception.InconsistentCreateDtoException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends GeneralCrudService<OrderDto, Order, Long, OrderDto> implements OrderService {

    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderPositionRepository orderPositionRepository;

    @Autowired
    protected OrderServiceImpl(GeneralCrudRepository<Order, Long> crudRepository,
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
        orderToCreate = crudRepository.save(orderToCreate);

        Set<OrderPosition> orderPositionsToCreate = receiveOrderPositionsIfAllGiftCertificatesExist(dto, orderToCreate);
        Set<OrderPosition> createdOrderPositions = new HashSet<>(orderPositionRepository
                .saveAll(orderPositionsToCreate));
        orderToCreate.setOrderPositions(createdOrderPositions);
        crudRepository.save(orderToCreate);

        return orderToCreate.getId();
    }

    @Override
    protected Example<Order> receiveUniqueConstraints(OrderDto dto) {
        return Example.of(new Order());
    }

    @Override
    protected Specification<Order> assembleJpaSpecification(SearchCriteriaDto<Order> searchCriteria) {
        if (searchCriteria == null) {
            return new OrderSpecification();
        }

        if (searchCriteria.getClass() != OrderSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        OrderSearchCriteriaDto params = (OrderSearchCriteriaDto) searchCriteria;
        return new OrderSpecification(params.getUserId());
    }

    @Override
    protected Order receiveUpdatingDomain(Order sourceDomain, OrderDto dto) {
        throw new RuntimeException("this method has not been implemented yet, because of task requirements");
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
        Optional<User> user = userRepository.findById(dto.getUser().getId());
        return user.orElseThrow(() -> new ResourceNotFoundException(dto.getUser().getId()));
    }

    private Order receiveOrderToCreate(User userToLink) {
        return new Order(Instant.now(), userToLink, null);
    }

    private Set<OrderPosition> receiveOrderPositionsIfAllGiftCertificatesExist(OrderDto dto, Order orderToCreate) {
        return dto.getOrderPositions().stream()
                .map(orderPosition -> {
                    Long certificateId = orderPosition.getGiftCertificate().getId();
                    Optional<GiftCertificate> certificate = giftCertificateRepository.findById(certificateId);
                    return certificate.orElseThrow(() -> new ResourceNotFoundException(certificateId));
                })
                .map(giftCertificate -> new OrderPosition(giftCertificate.getPrice(), orderToCreate, giftCertificate))
                .collect(Collectors.toSet());
    }

    @Override
    public OrderDto findOrderByIdIfBelongsToUser(Long orderId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
        Order order = crudRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(userId));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalRequestException();
        }
        return converter.convertToDto(order);
    }
}
