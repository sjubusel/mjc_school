package com.epam.esm.repository.old.mapper;

import com.epam.esm.model.domain.GeneralEntity;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;

public interface EntityMapper<T extends GeneralEntity<ID>, ID extends Serializable> extends RowMapper<T> {

}
