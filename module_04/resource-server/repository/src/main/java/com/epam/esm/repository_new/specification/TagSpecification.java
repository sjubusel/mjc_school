package com.epam.esm.repository_new.specification;

import com.epam.esm.model.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TagSpecification implements Specification<Tag> {

    private String name;

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate isDeletedCondition = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
        if (name == null) {
            return isDeletedCondition;
        }

        Predicate likeCondition = criteriaBuilder.like(root.get("name"), "%" + name + "%");

        return criteriaBuilder.and(likeCondition, isDeletedCondition);
    }
}
