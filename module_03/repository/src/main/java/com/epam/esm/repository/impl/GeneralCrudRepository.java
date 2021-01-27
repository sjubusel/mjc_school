package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.old.specification.SqlSpecification;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class GeneralCrudRepository<T extends GeneralEntity<ID>, ID extends Serializable>
        implements CrudRepository<T, ID> {

    protected final EntityManager entityManager;

    protected GeneralCrudRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * a method which retrieves a resource with <code>ID id</code>
     *
     * @param id an Object which is an identifier of a resource that is being searched for
     * @return an Optional object which represent a searched resource if it exists
     */
    @Override
    public Optional<T> findOne(ID id) {
        List<T> resultList = entityManager.createQuery(getCriteriaQueryReadById(id)).getResultList();
        return Optional.ofNullable(resultList.size() > 0 ? resultList.get(0) : null);
    }

    @Override
    public ID create(T entity) {
        return null;
    }

    @Override
    public Iterable<T> query(SqlSpecification specification) {
        return null;
    }

    @Override
    public boolean update(T entity) {
        return false;
    }

    @Override
    public boolean delete(ID id) {
        return false;
    }

    @Override
    public boolean exists(Map<String, Object> uniqueConstraints) {
        return false;
    }

    protected abstract CriteriaQuery<T> getCriteriaQueryReadById(ID id);
}
