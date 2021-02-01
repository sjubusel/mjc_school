package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.SearchCriteriaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultOrderService extends GeneralCrudService<OrderDto, Order, Long, OrderDto> implements OrderService {

    @Autowired
    protected DefaultOrderService(CrudRepository<Order, Long> crudRepository,
                                  GeneralEntityConverter<OrderDto, Order, Long> converter) {
        super(crudRepository, converter);
    }

    @Override
    protected Map<String, Object> receiveUniqueConstraints(OrderDto dto) {
        return null;
    }

    @Override
    protected JpaSpecification<Order, Long> getDataSourceSpecification(SearchCriteriaDto<Order> searchCriteria) {
        return null;
    }

    @Override
    protected Order receiveUpdatingDomain(Order sourceDomain, OrderDto dto) {
        return null;
    }
}
