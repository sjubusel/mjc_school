package com.epam.esm.repository.util.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.repository.util.PredicateBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

@Component
public class GiftCertificatePredicateBuilder implements PredicateBuilder<GiftCertificate, Long> {

    public Predicate buildExistsPredicate(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root,
                                          Map<String, Object> uniqueConstraints) {
        Predicate nameCondition = criteriaBuilder.equal(root.get("name"), uniqueConstraints.get("name"));
        Predicate descriptionCondition = criteriaBuilder.equal(root.get("description"),
                uniqueConstraints.get("description"));
        Predicate priceCondition = criteriaBuilder.equal(root.get("price"), uniqueConstraints.get("price"));
        Predicate durationCondition = criteriaBuilder.equal(root.get("duration"), uniqueConstraints.get("duration"));
        Predicate existsCondition = criteriaBuilder.equal(root.get("isDeleted"), Boolean.FALSE);
        return criteriaBuilder.and(nameCondition, descriptionCondition, priceCondition,
                durationCondition, existsCondition);
    }
}
