package com.epam.esm.repository.specification;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Optional;

public interface SqlSpecification {

    String toSql();

    Optional<SqlParameterSource> params();
}
