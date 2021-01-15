package com.epam.esm.repository;

import com.epam.esm.repository.specification.SqlSpecification;

import java.io.Serializable;
import java.util.Optional;

public interface CrudRepository<T, ID extends Serializable> {

    ID create(T entity);

    Iterable<T> query(SqlSpecification specification);

    Optional<T> findOne(ID id);

    void update(T entity);

    void delete(ID id);

    Long count();

    boolean exists(ID primaryKey);

    boolean exists(String mainUniqueValue);
}
