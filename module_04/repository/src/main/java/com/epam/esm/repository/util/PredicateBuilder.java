package com.epam.esm.repository.util;

import com.epam.esm.model.domain.Entity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Map;

public interface PredicateBuilder<T extends Entity<ID>, ID extends Serializable> {

    Predicate buildExistsPredicate(CriteriaBuilder criteriaBuilder, Root<T> root,
                                   Map<String, Object> uniqueConstraints);
}
