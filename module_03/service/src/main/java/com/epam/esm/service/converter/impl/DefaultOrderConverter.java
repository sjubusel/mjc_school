package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = DefaultOrderPositionConverter.class)
public interface DefaultOrderConverter extends GeneralEntityConverter<OrderDto, Order, Long> {

    @Mappings({
            @Mapping(target = "price", ignore = true),
            @Mapping(target = "orderDate", source = "orderDate", defaultExpression = "java(java.time.Instant.now())")
    })
    @Override
    Order convertToDomain(OrderDto dto);

    @Override
    OrderDto convertToDto(Order order);
}
