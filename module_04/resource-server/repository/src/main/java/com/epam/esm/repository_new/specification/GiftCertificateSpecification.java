package com.epam.esm.repository_new.specification;

import com.epam.esm.model.domain.GiftCertificate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GiftCertificateSpecification implements Specification<GiftCertificate> {

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
