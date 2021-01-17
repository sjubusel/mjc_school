package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.Entity;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.mapper.EntityMapper;
import com.epam.esm.repository.specification.SqlSpecification;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public abstract class BasicCrudRepository<T extends Entity<ID>, ID extends Serializable>
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

    protected abstract String getSqlQueryExistsName();

    @SuppressWarnings("unused")
    protected abstract Class<T> getClassForQuery();

    protected abstract SqlParameterSource getSqlParameterSource(T entity);

    protected abstract SqlParameterSource getSqlParameterSourceForUpdate(T entity);

    @SuppressWarnings("all")
    @Transactional
    @Override
    public ID create(T entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(getSqlQueryCreate(), getSqlParameterSource(entity), keyHolder);
        Long key = ((BigInteger) Objects.requireNonNull(keyHolder.getKey())).longValueExact();
        return ((ID) key);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<T> findOne(ID id) {
        List<T> result = namedParameterJdbcTemplate.query(getSqlQueryReadById(), new MapSqlParameterSource("id", id),
                rowMapper);
        T resource = result.size() > 0 ? result.get(0) : null;
        return Optional.ofNullable(resource);
    }

    @Transactional
    @Override
    public boolean update(T entity) {
        return namedParameterJdbcTemplate.update(getSqlQueryUpdate(entity),
                getSqlParameterSourceForUpdate(entity)) != 0;
    }

    @Transactional
    @Override
    public boolean delete(ID id) {
        return namedParameterJdbcTemplate.update(getSqlQueryDelete(), new MapSqlParameterSource("id", id)) != 0;
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<T> query(SqlSpecification specification) {
        SqlParameterSource params = specification.params();
        SqlParameterSource parameterSource = Optional.ofNullable(params).orElse(EmptySqlParameterSource.INSTANCE);
        return namedParameterJdbcTemplate.query(specification.toSql(), parameterSource, rowMapper);
    }

    @Transactional
    @Override
    public boolean exists(String uniqueConstraint) {
        List<T> result = namedParameterJdbcTemplate.query(getSqlQueryExistsName(),
                new MapSqlParameterSource("name", uniqueConstraint), rowMapper);
        return result.size() > 0;
    }
}
