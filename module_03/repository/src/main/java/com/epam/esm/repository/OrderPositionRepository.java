package com.epam.esm.repository;

import com.epam.esm.model.domain.OrderPosition;

import java.util.Set;

public interface OrderPositionRepository {

    Set<OrderPosition> createOrderPositions(Set<OrderPosition> orderPositionsToCreate);
}
