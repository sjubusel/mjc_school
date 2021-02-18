package com.epam.esm.repository.util.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.util.PredicateBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

@Component
public class TagPredicateBuilder implements PredicateBuilder<Tag, Long> {

    @Override
    public Predicate buildExistsPredicate(CriteriaBuilder criteriaBuilder, Root<Tag> root,
                                          Map<String, Object> uniqueConstraints) {
        Predicate nameCondition = criteriaBuilder.equal(root.get("name"), uniqueConstraints.get("name"));
        Predicate existsCondition = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
        return criteriaBuilder.and(nameCondition, existsCondition);
    }
}
