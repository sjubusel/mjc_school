package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.specification.JpaSpecification;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
        entityManager.persist(entity);
        return entity.getId();
    }

    @Override
    public Iterable<T> query(JpaSpecification<T, ID> specification) {
        return specification.toQuery(entityManager).getResultList();
    }

    @Override
    public boolean update(T entity) {
        return getUpdateQuery(entity).executeUpdate() == 1;
    }

    @Override
    public boolean delete(ID id) {
        return false;
    }

    @Override
    public boolean exists(Map<String, Object> uniqueConstraints) {
        List<T> resultList = entityManager.createQuery(getCriteriaQueryExists(uniqueConstraints)).getResultList();
        return !resultList.isEmpty();
    }

    protected abstract CriteriaQuery<T> getCriteriaQueryReadById(ID id);

    protected abstract CriteriaQuery<T> getCriteriaQueryExists(Map<String, Object> uniqueConstraints);

    protected abstract Query getUpdateQuery(T entity);
}
