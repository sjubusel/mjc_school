package com.epam.esm.repository.old.mapper;

import com.epam.esm.model.domain.Tag;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * a class which maps any com.epam.esm.model.domain.Tag object and its database analogy
 * while using org.springframework.jdbc.core.JdbcTemplate
 */
public class TagMapper implements EntityMapper<Tag, Long> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();

        tag.setId(rs.getLong("id"));
        tag.setName(rs.getString("name"));

        return tag;
    }
}
