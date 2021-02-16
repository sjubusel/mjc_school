package com.epam.esm.repository.specification.impl;

import com.epam.esm.model.domain.Order;
import com.epam.esm.repository.specification.JpaSpecification;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class OrderSpecification implements JpaSpecification<Order, Long> {

    private Integer page;
    private Integer pageSize;
    private Long userId;

    @Override
    public TypedQuery<Order> toQuery(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        Join<Object, Object> userJoin = root.join("user", JoinType.INNER);
        Join<Object, Object> positionsJoin = root.join("orderPositions", JoinType.INNER);
        positionsJoin.join("giftCertificate", JoinType.INNER);

        if (userId != null) {
            criteriaQuery.select(root).where(criteriaBuilder.equal(userJoin.get("id"), userId)).distinct(true);
        } else {
            criteriaQuery.select(root).distinct(true);
        }

        if (page == null) {
            page = 1;
        }

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageSize * (page - 1))
                .setMaxResults(pageSize);
    }
}
