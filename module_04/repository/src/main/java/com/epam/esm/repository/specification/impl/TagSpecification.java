package com.epam.esm.repository.specification.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.specification.JpaSpecification;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class TagSpecification implements JpaSpecification<Tag, Long> {

    private String name;
    private Integer pageSize;
    private Integer page;

    @Override
    public TypedQuery<Tag> toQuery(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        if (Stream.of(name, page).allMatch(Objects::isNull)) {
            TypedQuery<Tag> query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(0);
            query.setMaxResults(pageSize);
            return query;
        }

        adjustCriteriaQuery(criteriaBuilder, criteriaQuery, root);

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageSize * (page - 1))
                .setMaxResults(pageSize);
    }

    private void adjustCriteriaQuery(CriteriaBuilder criteriaBuilder, CriteriaQuery<Tag> criteriaQuery,
                                     Root<Tag> root) {
        Predicate likeCondition = null;
        Predicate isDeletedCondition = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);

        if (name != null) {
            likeCondition = criteriaBuilder.like(root.get("name"), "%" + name + "%");
        }
        if (likeCondition != null) {
            Predicate condition = criteriaBuilder.and(likeCondition, isDeletedCondition);
            criteriaQuery.select(root).where(condition);
        } else {
            criteriaQuery.select(root).where(isDeletedCondition);
        }

        if (page == null) {
            page = 1;
        }
    }
}
