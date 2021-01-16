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

    protected abstract Class<T> getClassForQuery();

    protected abstract SqlParameterSource getSqlParameterSource(T entity);

    protected abstract SqlParameterSource getSqlParameterSourceForUpdate(T entity);

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public ID create(T entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(getSqlQueryCreate(), getSqlParameterSource(entity), keyHolder);
        return ((ID) Objects.requireNonNull(keyHolder.getKeys()).get("id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<T> findOne(ID id) {
        T entity = namedParameterJdbcTemplate.queryForObject(getSqlQueryReadById(),
                new MapSqlParameterSource("id", id), rowMapper);
        return Optional.ofNullable(entity);
    }

    @Transactional
    @Override
    public void update(T entity) {
        namedParameterJdbcTemplate.update(getSqlQueryUpdate(entity), getSqlParameterSourceForUpdate(entity));
    }

    @Transactional
    @Override
    public void delete(ID id) {
        namedParameterJdbcTemplate.update(getSqlQueryDelete(), new MapSqlParameterSource("id", id));
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
    public boolean exists(String mainUniqueValue) {
        Long entityId = namedParameterJdbcTemplate.queryForObject(getSqlQueryExistsName(),
                new MapSqlParameterSource("name", mainUniqueValue), Long.class);
        return entityId != null;
    }

    @Transactional
    @Override
    public Long count() {
        throw new RuntimeException(); // TODO realize a custom exception
    }

    @Transactional
    @Override
    public boolean exists(ID primaryKey) {
        throw new RuntimeException(); // TODO realize a custom exception
    }
}
