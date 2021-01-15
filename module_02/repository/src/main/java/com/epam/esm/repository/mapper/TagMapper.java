package com.epam.esm.repository.mapper;

import com.epam.esm.model.domain.Tag;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * a class which maps any com.epam.esm.model.domain.Tag object and its database analogy
 * while using org.springframework.jdbc.core.JdbcTemplate
 */
@Component
public class TagMapper implements EntityMapper<Tag, Long> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();

        tag.setId(rs.getLong("tag_id"));
        tag.setName(rs.getString("name"));

        return tag;
    }
}
