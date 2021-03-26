package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.model.domain.OrderPosition;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.Set;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {OrderPositionConverter.class, UserConverter.class})
public interface OrderConverter extends GeneralEntityConverter<OrderDto, Order, Long> {

    @Mappings({
            @Mapping(target = "orderDate", source = "orderDate", defaultExpression = "java(java.time.Instant.now())")
    })
    @Override
    Order convertToDomain(OrderDto dto);

    @Mappings({
            @Mapping(target = "price", source = "orderPositions", qualifiedByName = "receiveTotalPrice")
    })
    @Override
    OrderDto convertToDto(Order order);

    @Named("receiveTotalPrice")
    default BigDecimal receiveTotalPrice(Set<OrderPosition> orderPositions) {
        return orderPositions.stream()
                .map(OrderPosition::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
