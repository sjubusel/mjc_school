package com.epam.esm.repository.specification.impl;

import com.epam.esm.model.domain.User;
import com.epam.esm.repository.specification.JpaSpecification;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@NoArgsConstructor
@AllArgsConstructor
public class UserSpecification implements JpaSpecification<User, Long> {
    private static final Integer PAGE_SIZE = 20;

    private Integer page;

    @Override
    public TypedQuery<User> toQuery(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);

        if (page == null) {
            page = 1;
        }

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(PAGE_SIZE * (page - 1))
                .setMaxResults(PAGE_SIZE);
    }
}
