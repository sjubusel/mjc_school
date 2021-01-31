package com.epam.esm.repository.specification.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.specification.JpaSpecification;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
public class TagSpecification implements JpaSpecification<Tag, Long> {
    private static final Integer PAGE_SIZE = 20;

    private String name;
    private Integer page;

    @Override
    public TypedQuery<Tag> toQuery(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        if (Stream.of(name, page).allMatch(Objects::isNull)) {
            TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(0);
            query.setMaxResults(PAGE_SIZE);
            return query;
        }

        adjustCriteriaQuery(criteriaBuilder, criteriaQuery, root);

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(PAGE_SIZE * (page - 1))
                .setMaxResults(PAGE_SIZE);
    }

    private void adjustCriteriaQuery(CriteriaBuilder criteriaBuilder, CriteriaQuery<Tag> criteriaQuery,
                                     Root<Tag> root) {
        Predicate likeCondition = null;
        if (name != null) {
            likeCondition = criteriaBuilder.like(root.get("name"), "%" + name + "%");
        }
        if (likeCondition != null) {
            criteriaQuery.select(root).where(likeCondition);
        } else {
            criteriaQuery.select(root);
        }

        if (page == null) {
            page = 1;
        }
    }
}
