package com.epam.esm.repository.util;

import com.epam.esm.model.domain.User;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

@Component
public class UserPredicateBuilder {

    public Predicate buildExistsPredicate(CriteriaBuilder criteriaBuilder, Root<User> root,
                                          Map<String, Object> uniqueConstraints) {
        Predicate loginCondition = criteriaBuilder.equal(root.get("login"), uniqueConstraints.get("login"));
        Predicate phoneNumberCondition = criteriaBuilder.equal(root.get("phoneNumber"),
                uniqueConstraints.get("phoneNumber"));
        Predicate email = criteriaBuilder.equal(root.get("email"), uniqueConstraints.get("email"));
        return criteriaBuilder.or(loginCondition, phoneNumberCondition, email);
    }
}
