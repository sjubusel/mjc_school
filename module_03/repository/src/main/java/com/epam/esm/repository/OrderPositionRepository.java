package com.epam.esm.repository;

import com.epam.esm.model.domain.OrderPosition;

import java.util.Set;

public interface OrderPositionRepository {

    void createOrderPositions(Set<OrderPosition> orderPositionsToCreate);
}
