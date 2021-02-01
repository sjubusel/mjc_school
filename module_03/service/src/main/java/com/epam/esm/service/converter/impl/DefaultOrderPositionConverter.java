package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.OrderPosition;
import com.epam.esm.model.dto.OrderPositionDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {DefaultGiftCertificateConverter.class, DefaultOrderConverter.class})
public interface DefaultOrderPositionConverter extends GeneralEntityConverter<OrderPositionDto, OrderPosition, Long> {

    @Mappings({
            @Mapping(target = "price", ignore = true),
            @Mapping(target = "order", ignore = true)
    })
    @Override
    OrderPosition convertToDomain(OrderPositionDto dto);

    @Override
    OrderPositionDto convertToDto(OrderPosition orderPosition);
}
