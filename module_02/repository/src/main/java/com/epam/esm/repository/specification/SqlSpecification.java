package com.epam.esm.repository.specification;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public interface SqlSpecification {

    String toSql();

    SqlParameterSource params();
}
