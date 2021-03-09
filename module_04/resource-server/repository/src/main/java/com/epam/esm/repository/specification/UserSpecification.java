package com.epam.esm.repository.specification;

import com.epam.esm.model.domain.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {

    /**
     * currently a stub method, as there is no
     * @param root an object-representation of the source table
     * @param query a Criteria-API query
     * @param criteriaBuilder an object which assembles the returnObject
     * @return an object which represents WHERE-condition of <code>query</code>
     */
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
