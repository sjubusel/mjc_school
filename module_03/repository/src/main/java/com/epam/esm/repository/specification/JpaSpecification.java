package com.epam.esm.repository.specification;

import com.epam.esm.model.domain.GeneralEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;

public interface JpaSpecification<T extends GeneralEntity<ID>, ID extends Serializable> {

    TypedQuery<T> toQuery(EntityManager entityManager);
}
