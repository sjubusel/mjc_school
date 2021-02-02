package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.OrderPosition;
import com.epam.esm.repository.OrderPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class DefaultOrderPositionRepository implements OrderPositionRepository {

    private final EntityManager entityManager;

    @Override
    public void createOrderPositions(Set<OrderPosition> orderPositionsToCreate) {
        orderPositionsToCreate.forEach(entityManager::persist);
    }
}
