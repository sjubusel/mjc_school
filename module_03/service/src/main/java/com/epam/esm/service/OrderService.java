package com.epam.esm.service;

import com.epam.esm.model.domain.Order;
import com.epam.esm.model.dto.OrderDto;

public interface OrderService extends CrudService<OrderDto, Order, Long, OrderDto> {

    OrderDto findOrderByIdIfBelongsToUser(Long orderId, Long userId);
}
