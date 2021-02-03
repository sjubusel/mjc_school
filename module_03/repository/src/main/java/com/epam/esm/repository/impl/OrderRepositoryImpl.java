package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Map;

@Repository
public class OrderRepositoryImpl extends GeneralCrudRepository<Order, Long> implements OrderRepository {

    @Autowired
    protected OrderRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected CriteriaQuery<Order> getCriteriaQueryReadById(Long idToFind) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

        Root<Order> root = criteriaQuery.from(Order.class);
        Join<Object, Object> positions = root.join("orderPositions", JoinType.INNER);
        positions.join("giftCertificate", JoinType.INNER);
        root.join("user", JoinType.INNER);

        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), idToFind));
        criteriaQuery.select(root);

        return criteriaQuery;
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
