package com.epam.esm.repository.specification;

import com.epam.esm.model.domain.Order;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderSpecification implements Specification<Order> {

    private Long userId;

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (userId == null) {
            return null;
        }

        Join<Object, Object> userJoin = root.join("user", JoinType.INNER);
        Join<Object, Object> positionsJoin = root.join("orderPositions", JoinType.INNER);
        positionsJoin.join("giftCertificate", JoinType.INNER);
        query.distinct(true);

        return criteriaBuilder.equal(userJoin.get("id"), userId);
    }
}
