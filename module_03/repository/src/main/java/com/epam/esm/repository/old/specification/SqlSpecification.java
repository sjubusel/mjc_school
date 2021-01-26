package com.epam.esm.repository.old.specification;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public interface SqlSpecification {

    String toSql();

    SqlParameterSource params();
}
