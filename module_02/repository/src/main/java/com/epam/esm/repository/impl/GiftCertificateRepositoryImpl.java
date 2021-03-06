package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl extends BasicCrudRepository<GiftCertificate, Long>
        implements GiftCertificateRepository {

    private static final String INSERT_NEW_GIFT_CERTIFICATE = "INSERT INTO gift_certificates_system.certificates " +
            "(name, description, price, duration, create_date, last_update_date) " +
            "VALUES (:name, :description, :price, :duration, :createDate, :lastUpdateDate)";
    private static final String SELECT_GIFT_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificates_system.certificates " +
            "WHERE certificate_id = :id";
    private static final String UPDATE_GIFT_CERTIFICATE_START = "UPDATE gift_certificates_system.certificates " +
            "certificate SET certificate.last_update_date=:lastUpdateDate";
    public static final String UPDATE_GIFT_CERTIFICATE_ENG = " WHERE certificate.certificate_id=:id";
    private static final String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificates_system.certificates " +
            "certificate WHERE certificate.certificate_id=:id";
    private static final String LINK_CERTIFICATE_WITH_TAG
            = "INSERT INTO gift_certificates_system.join_certificates_tags_table (certificate_id, tag_id) " +
            "SELECT :certificateId, tag.tag_id  FROM gift_certificates_system.tags tag WHERE tag.name = :tagName";
    private static final String DELETE_LINK_BETWEEN_GIFT_CERTIFICATE_AND_TAGS
            = "DELETE FROM gift_certificates_system.join_certificates_tags_table join_table " +
            "WHERE join_table.certificate_id = :id";
    private static final String SELECT_GIFT_CERTIFICATE_ID_BY_NAME = "SELECT * " +
            "FROM gift_certificates_system.certificates certificate WHERE certificate.name = :name";

    @Autowired
    public GiftCertificateRepositoryImpl(EntityMapper<GiftCertificate, Long> rowMapper,
                                         NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(rowMapper, namedParameterJdbcTemplate);
    }

    @Override
    public void linkGiftCertificateWithTags(Long certificateId, List<Tag> updatingTags) {
        updatingTags.forEach(tag -> {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("certificateId", certificateId);
            parameterSource.addValue("tagName", tag.getName());

            namedParameterJdbcTemplate.update(LINK_CERTIFICATE_WITH_TAG, parameterSource);
        });
    }

    @Override
    public void deleteLinkBetweenGiftCertificateAndTags(Long certificateId) {
        namedParameterJdbcTemplate.update(DELETE_LINK_BETWEEN_GIFT_CERTIFICATE_AND_TAGS,
                new MapSqlParameterSource("id", certificateId));
    }

    @Override
    protected String getSqlQueryCreate() {
        return INSERT_NEW_GIFT_CERTIFICATE;
    }

    @Override
    protected String getSqlQueryReadById() {
        return SELECT_GIFT_CERTIFICATE_BY_ID;
    }

    @Override
    protected String getSqlQueryUpdate(GiftCertificate certificate) {
        StringBuilder setSection = new StringBuilder();

        String setDelimiter = ", ";
        Optional.ofNullable(certificate.getName()).ifPresent(s -> setSection.append(setDelimiter)
                .append("certificate.name=:name"));
        Optional.ofNullable(certificate.getDescription()).ifPresent(s -> setSection.append(setDelimiter)
                .append("certificate.description=:description"));
        Optional.ofNullable(certificate.getPrice()).ifPresent(s -> setSection.append(setDelimiter)
                .append("certificate.price=:price"));
        Optional.ofNullable(certificate.getDuration()).ifPresent(s -> setSection.append(setDelimiter)
                .append("certificate.duration=:duration"));

        return UPDATE_GIFT_CERTIFICATE_START + new String(setSection) + UPDATE_GIFT_CERTIFICATE_ENG;
    }

    @Override
    protected String getSqlQueryDelete() {
        return DELETE_GIFT_CERTIFICATE_BY_ID;
    }

    @Override
    protected String getSqlQueryExists() {
        return SELECT_GIFT_CERTIFICATE_ID_BY_NAME;
    }

    @Override
    protected SqlParameterSource getSqlParameterSource(GiftCertificate certificate) {
        return new MapSqlParameterSource()
                .addValue("name", certificate.getName())
                .addValue("description", certificate.getDescription())
                .addValue("price", certificate.getPrice())
                .addValue("duration", certificate.getDuration())
                .addValue("createDate", Timestamp.from(certificate.getCreateDate()))
                .addValue("lastUpdateDate", Timestamp.from(certificate.getUpdateDate()));
    }

    @Override
    protected SqlParameterSource getSqlParameterSourceForUpdate(GiftCertificate certificate) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", certificate.getId());

        Optional.ofNullable(certificate.getName()).ifPresent(s -> parameterSource.addValue("name", s));
        Optional.ofNullable(certificate.getDescription()).ifPresent(s -> parameterSource.addValue("description", s));
        Optional.ofNullable(certificate.getPrice()).ifPresent(s -> parameterSource.addValue("price", s));
        Optional.ofNullable(certificate.getDuration()).ifPresent(s -> parameterSource.addValue("duration", s));
        Optional.ofNullable(certificate.getUpdateDate()).ifPresent(s -> parameterSource.addValue("lastUpdateDate",
                Timestamp.from(s)));

        return parameterSource;
    }
}
