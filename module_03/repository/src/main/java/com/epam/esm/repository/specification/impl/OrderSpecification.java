package com.epam.esm.repository.specification.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.repository.specification.JpaSpecification;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

@NoArgsConstructor
@AllArgsConstructor
public class OrderSpecification implements JpaSpecification<Order, Long> {

    private static final Integer PAGE_SIZE = 20;

    private Integer page;


    @Override
    public TypedQuery<Order> toQuery(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        root.join("user", JoinType.INNER);
        Join<Object, Object> positions = root.join("orderPositions", JoinType.INNER);
        positions.join("giftCertificate", JoinType.INNER);

        criteriaQuery.select(root).distinct(true);

        if (page == null) {
            page = 1;
        }

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(PAGE_SIZE * (page - 1))
                .setMaxResults(PAGE_SIZE);
    }
}
