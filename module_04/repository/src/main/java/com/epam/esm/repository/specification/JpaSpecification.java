package com.epam.esm.repository.specification;

import com.epam.esm.model.domain.Entity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;

public interface JpaSpecification<T extends Entity<ID>, ID extends Serializable> {

    TypedQuery<T> toQuery(EntityManager entityManager);
}
