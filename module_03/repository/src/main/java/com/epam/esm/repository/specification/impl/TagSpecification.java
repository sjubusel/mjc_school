package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.SqlSpecification;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class TagSpecification implements SqlSpecification {

    private static final String SELECT_TAGS = "SELECT tag.id, tag.name from gift_certificates_system.tags tag";
    private static final String WHITESPACE = " ";

    private String name;
    private MapSqlParameterSource parameterSource;

    public TagSpecification() {
    }

    public TagSpecification(String searchName) {
        if (searchName == null) {
            return;
        }

        this.name = searchName;
        parameterSource = new MapSqlParameterSource("name", "%" + searchName + "%");
    }

    @Override
    public String toSql() {
        return (name == null) ? SELECT_TAGS : generateQuery();
    }

    @Override
    public SqlParameterSource params() {
        return parameterSource;
    }

    private String generateQuery() {
        StringBuilder sb = new StringBuilder(SELECT_TAGS).append(WHITESPACE).append("WHERE tag.name LIKE :name");
        return new String(sb);
    }
}
