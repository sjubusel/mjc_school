package com.epam.esm.repository.util;

import com.epam.esm.model.domain.GiftCertificate;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

@Component
public class GiftCertificatePredicateBuilder {

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
