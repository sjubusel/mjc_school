package com.epam.esm.repository.old.impl;

import com.epam.esm.model.domain.GeneralEntity;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.old.mapper.EntityMapper;
import com.epam.esm.repository.old.specification.SqlSpecification;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public abstract class BasicCrudRepository<T extends GeneralEntity<ID>, ID extends Serializable>
        implements CrudRepository<T, ID> {

    protected final EntityMapper<T, ID> rowMapper;

    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BasicCrudRepository(EntityMapper<T, ID> rowMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.rowMapper = rowMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    protected abstract String getSqlQueryCreate();

    protected abstract String getSqlQueryReadById();

    protected abstract String getSqlQueryUpdate(T entity);

    protected abstract String getSqlQueryDelete();

    protected abstract String getSqlQueryExists();

    protected abstract SqlParameterSource getSqlParameterSource(T entity);

    protected abstract SqlParameterSource getSqlParameterSourceForUpdate(T entity);

    /**
     * a standard CREATE method which saves a resource in a datasource (a MySql database).
     * A cast to java.math.BigInteger is necessary because the main MySql database returns "BIGINT SQL TYPE" as
     * java.math.BigInteger, not as the majority of other databases, which return java.lang.Long.
     * SuppressWarnings annotation is forced because of an aforementioned a java.math.BigInteger return type.
     *
     * @param entity a resource which is to be saved in the datasource
     * @return an Object which is an identifier of a newly created resource
     */
    @SuppressWarnings("all")
    @Override
    public ID create(T entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(getSqlQueryCreate(), getSqlParameterSource(entity), keyHolder);
        Long key = ((BigInteger) Objects.requireNonNull(keyHolder.getKey())).longValueExact();
        return ((ID) key);
    }

    /**
     * a method which retrieves a resource with <code>ID id</code>
     *
     * @param id an Object which is an identifier of a resource that is being searched for
     * @return an Optional object which represent a searched resource if it exists
     */
    @Override
    public Optional<T> findOne(ID id) {
        List<T> result = namedParameterJdbcTemplate.query(getSqlQueryReadById(), new MapSqlParameterSource("id", id),
                rowMapper);
        T resource = result.size() > 0 ? result.get(0) : null;
        return Optional.ofNullable(resource);
    }

    /**
     * a method which modifies a resource with its new state containing in a domain object <code>T entity</code>
     *
     * @param entity a domain object with new state of a resource
     * @return true if updating is created successfully
     */
    @Override
    public boolean update(T entity) {
        return namedParameterJdbcTemplate.update(getSqlQueryUpdate(entity),
                getSqlParameterSourceForUpdate(entity)) != 0;
    }

    /**
     * a method which removes a resource with <code>ID id</code> from a datasource
     *
     * @param id an Object which is an identifier of a resource that is being searched for
     * @return true if a resource is successfully removed
     */
    @Override
    public boolean delete(ID id) {
        return namedParameterJdbcTemplate.update(getSqlQueryDelete(), new MapSqlParameterSource("id", id)) != 0;
    }

    /**
     * a method which finds resources with certain state by creating an adaptive SELECT statement on the basic of
     * <code>SqlSpecification specification</code>
     *
     * @param specification a dto which contains logic of creation of the aforementioned SELECT statement
     * @return a collection of resources which correspond to search criteria
     */
    @Override
    public Iterable<T> query(SqlSpecification specification) {
        SqlParameterSource params = specification.params();
        SqlParameterSource parameterSource = Optional.ofNullable(params).orElse(EmptySqlParameterSource.INSTANCE);
        return namedParameterJdbcTemplate.query(specification.toSql(), parameterSource, rowMapper);
    }

    /**
     * a method which verifies an existence of a resource in a datasource by evaluating of unique constraints,
     *
     * @param uniqueConstraints a Map containing pairs of "key - value"
     * @return true if a resource exists in a database
     */
    @Override
    public boolean exists(Map<String, Object> uniqueConstraints) {
        List<T> result = namedParameterJdbcTemplate.query(getSqlQueryExists(),
                new MapSqlParameterSource(uniqueConstraints), rowMapper);
        return result.size() > 0;
    }
}
