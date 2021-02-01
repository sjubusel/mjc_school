package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Map;

@Repository
public class DefaultOrderRepository extends GeneralCrudRepository<Order, Long> implements OrderRepository {

    @Autowired
    protected DefaultOrderRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected CriteriaQuery<Order> getCriteriaQueryReadById(Long aLong) {
        return null;
    }

    @Override
    protected CriteriaQuery<Order> getCriteriaQueryExists(Map<String, Object> uniqueConstraints) {
        return null;
    }

    @Override
    protected Query getDeleteQuery(Long aLong) {
        return null;
    }
}
