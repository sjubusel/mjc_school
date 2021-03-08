package com.epam.esm.repository_new.specification;

import com.epam.esm.model.domain.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TagSpecification implements Specification<Tag> {

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
