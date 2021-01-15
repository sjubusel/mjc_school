package com.epam.esm.repository.mapper;

import com.epam.esm.model.domain.Entity;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;

public interface EntityMapper<T extends Entity<ID>, ID extends Serializable> extends RowMapper<T> {

}
