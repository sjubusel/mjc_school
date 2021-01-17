package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl extends BasicCrudRepository<Tag, Long> implements TagRepository {

    private static final String INSERT_NEW_TAG = "INSERT INTO gift_certificates_system.tags (name) VALUES (:name)";
    private static final String SELECT_TAG_BY_ID = "SELECT * FROM gift_certificates_system.tags t WHERE t.tag_id = :id";
    private static final String UPDATE_TAG = "UPDATE gift_certificates_system.tags SET name = :name WHERE tag_id = :id";
    private static final String DELETE_TAG_BY_ID = "DELETE FROM gift_certificates_system.tags WHERE tag_id = :id";
    public static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT * FROM gift_certificates_system.tags t " +
            "JOIN gift_certificates_system.join_certificates_tags_table jt ON t.tag_id = jt.tag_id " +
            "WHERE jt.certificate_id = ?";
    private static final String SELECT_TAG_ID_BY_NAME = "SELECT * FROM gift_certificates_system.tags t " +
            "WHERE t.name = :name";
    private static final String DELETE_LINK_BETWEEN_TAG_AND_GIFT_CERTIFICATES
            = "DELETE FROM gift_certificates_system.join_certificates_tags_table jctt WHERE jctt.tag_id = :id";

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
    public void createIfNotExist(List<Tag> tags) {
        tags.stream()
                .filter(tag -> !exists(tag.getName()))
                .forEach(this::create);
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
    protected String getSqlQueryExistsName() {
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
