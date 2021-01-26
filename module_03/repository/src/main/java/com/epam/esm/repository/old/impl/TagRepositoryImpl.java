package com.epam.esm.repository.old.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.old.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class TagRepositoryImpl extends BasicCrudRepository<Tag, Long> implements TagRepository {

    private static final String INSERT_NEW_TAG = "INSERT INTO gift_certificates_system.tags (name) VALUES (:name)";
    private static final String SELECT_TAG_BY_ID = "SELECT * FROM gift_certificates_system.tags tag " +
            "WHERE tag.id = :id";
    private static final String UPDATE_TAG = "UPDATE gift_certificates_system.tags SET name = :name WHERE id = :id";
    private static final String DELETE_TAG_BY_ID = "DELETE FROM gift_certificates_system.tags WHERE id = :id";
    public static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT * FROM gift_certificates_system.tags tag " +
            "JOIN gift_certificates_system.join_certificates_tags_table join_table ON tag.id = join_table.tag_id " +
            "WHERE join_table.certificate_id = ?";
    private static final String SELECT_TAG_ID_BY_NAME = "SELECT * FROM gift_certificates_system.tags tag " +
            "WHERE tag.name = :name";
    private static final String DELETE_LINK_BETWEEN_TAG_AND_GIFT_CERTIFICATES
            = "DELETE FROM gift_certificates_system.join_certificates_tags_table join_table " +
            "WHERE join_table.tag_id = :id";

    @Autowired
    public TagRepositoryImpl(EntityMapper<Tag, Long> rowMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(rowMapper, namedParameterJdbcTemplate);
    }

    @Override
    public List<Tag> receiveTagsByGiftCertificateId(Long id) {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SELECT_TAGS_BY_CERTIFICATE_ID, rowMapper, id);
    }

    @Override
    public void deleteLinkBetweenTagAndGiftCertificates(Long tagId) {
        namedParameterJdbcTemplate.update(DELETE_LINK_BETWEEN_TAG_AND_GIFT_CERTIFICATES,
                new MapSqlParameterSource("id", tagId));
    }

    @Override
    public boolean exists(String uniqueConstraint) {
        return exists(Collections.singletonMap("name", uniqueConstraint));
    }

    @Override
    protected String getSqlQueryCreate() {
        return INSERT_NEW_TAG;
    }

    @Override
    protected String getSqlQueryReadById() {
        return SELECT_TAG_BY_ID;
    }

    @Override
    protected String getSqlQueryUpdate(Tag tag) {
        return UPDATE_TAG;
    }

    @Override
    protected String getSqlQueryDelete() {
        return DELETE_TAG_BY_ID;
    }

    @Override
    protected String getSqlQueryExists() {
        return SELECT_TAG_ID_BY_NAME;
    }

    @Override
    protected SqlParameterSource getSqlParameterSource(Tag tag) {
        return new MapSqlParameterSource().addValue("id", tag.getId()).addValue("name", tag.getName());
    }

    @Override
    protected SqlParameterSource getSqlParameterSourceForUpdate(Tag tag) {
        return getSqlParameterSource(tag);
    }
}
