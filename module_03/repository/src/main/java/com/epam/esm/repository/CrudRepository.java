package com.epam.esm.repository;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.repository.specification.JpaSpecification;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public interface CrudRepository<T extends GeneralEntity<ID>, ID extends Serializable> {

    ID create(T entity);

    Iterable<T> query(JpaSpecification<T, ID> specification);

    Optional<T> findOne(ID id);

    boolean update(T entity);

    boolean delete(ID id);

    boolean exists(Map<String, Object> uniqueConstraints);
}
