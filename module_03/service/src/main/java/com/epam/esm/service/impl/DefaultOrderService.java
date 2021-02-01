package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.OrderSpecification;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.service.dto.SearchCriteriaDto;
import com.epam.esm.service.exception.IncompatibleSearchCriteriaException;
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
        return EMPTY_UNIQUE_CONSTRAINTS;
    }

    @Override
    protected JpaSpecification<Order, Long> getDataSourceSpecification(SearchCriteriaDto<Order> searchCriteria) {
        if (searchCriteria == null) {
            return new OrderSpecification();
        }

        if (searchCriteria.getClass() != OrderSearchCriteriaDto.class) {
            throw new IncompatibleSearchCriteriaException("Incompatible type of SearchCriteriaDto is passed");
        }

        OrderSearchCriteriaDto params = (OrderSearchCriteriaDto) searchCriteria;
        return new OrderSpecification(params.getPage());
    }

    @Override
    protected Order receiveUpdatingDomain(Order sourceDomain, OrderDto dto) {
        return null;
    }
}
