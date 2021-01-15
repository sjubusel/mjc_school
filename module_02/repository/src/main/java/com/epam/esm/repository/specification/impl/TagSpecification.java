package com.epam.esm.repository.specification.impl;

import com.epam.esm.model.dto.TagSearchCriteriaDto;
import com.epam.esm.repository.specification.SqlSpecification;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Optional;

public class TagSpecification implements SqlSpecification {

    private static final String SELECT_TAGS = "SELECT t.tag_id, t.name from gift_certificates_system.tags t";
    private static final String WHITESPACE = " ";

    private String name;
    private MapSqlParameterSource parameterSource;

    public TagSpecification() {
    }

    public TagSpecification(TagSearchCriteriaDto searchCriteriaDto) {
        this.name = searchCriteriaDto.getName();
        parameterSource = new MapSqlParameterSource("name", name);
    }

    @Override
    public String toSql() {
        return (name == null) ? SELECT_TAGS : generateQuery();
    }

    @Override
    public Optional<SqlParameterSource> params() {
        return Optional.ofNullable(parameterSource);
    }

    private String generateQuery() {
        StringBuilder sb = new StringBuilder(SELECT_TAGS).append(WHITESPACE).append("WHERE t.name LIKE :name");
        return new String(sb);
    }
}
