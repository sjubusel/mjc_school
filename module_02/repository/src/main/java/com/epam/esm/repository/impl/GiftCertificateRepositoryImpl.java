package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl extends BasicCrudRepository<GiftCertificate, Long>
        implements GiftCertificateRepository {

    private static final String INSERT_NEW_GIFT_CERTIFICATE = "INSERT INTO gift_certificates_system.certificates " +
            "(name, description, price, duration, create_date, last_update_date) " +
            "VALUES (:name, :description, :price, :duration, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
    private static final String SELECT_GIFT_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificates_system.certificates " +
            "WHERE certificate_id = :id";
    private static final String UPDATE_GIFT_CERTIFICATE_START = "UPDATE gift_certificates_system.certificates c " +
            "SET c.last_update_date=CURRENT_TIMESTAMP";
    public static final String UPDATE_GIFT_CERTIFICATE_ENG = " WHERE c.id=:id";
    private static final String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE gift_certificates_system.certificates c " +
            "WHERE c.id=:id";
    private static final String LINK_CERTIFICATE_WITH_TAG
            = "INSERT INTO gift_certificates_system.join_certificates_tags_table (certificate_id, tag_id) " +
            "VALUES (:certificateId, :tagId)";
    private static final String DELETE_LINK_BETWEEN_GIFT_CERTIFICATE_AND_TAGS
            = "DELETE FROM gift_certificates_system.join_certificates_tags_table jctt WHERE jctt.certificate_id = :id";
    private static final String SELECT_GIFT_CERTIFICATE_ID_BY_NAME = "SELECT * " +
            "FROM gift_certificates_system.certificates c WHERE c.name = :name";

    @Autowired
    public GiftCertificateRepositoryImpl(EntityMapper<GiftCertificate, Long> rowMapper,
                                         NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(rowMapper, namedParameterJdbcTemplate);
    }

    @Transactional
    @Override
    public void linkCertificateWithTag(Long certificateId, Long tagId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("certificateId", certificateId);
        parameterSource.addValue("tagId", tagId);

        namedParameterJdbcTemplate.update(LINK_CERTIFICATE_WITH_TAG, parameterSource);
    }

    @Transactional
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
                .append("c.name=:name"));
        Optional.ofNullable(certificate.getDescription()).ifPresent(s -> setSection.append(setDelimiter)
                .append("c.description=:description"));
        Optional.ofNullable(certificate.getPrice()).ifPresent(s -> setSection.append(setDelimiter)
                .append("c.price=:price"));
        Optional.ofNullable(certificate.getDuration()).ifPresent(s -> setSection.append(setDelimiter)
                .append("c.duration=:duration"));

        return UPDATE_GIFT_CERTIFICATE_START + new String(setSection) + UPDATE_GIFT_CERTIFICATE_ENG;
    }

    @Override
    protected String getSqlQueryDelete() {
        return DELETE_GIFT_CERTIFICATE_BY_ID;
    }

    @Override
    protected String getSqlQueryExistsName() {
        return SELECT_GIFT_CERTIFICATE_ID_BY_NAME;
    }

    @Override
    protected Class<GiftCertificate> getClassForQuery() {
        return GiftCertificate.class;
    }

    @Override
    protected SqlParameterSource getSqlParameterSource(GiftCertificate certificate) {
        return new MapSqlParameterSource()
                .addValue("name", certificate.getName())
                .addValue("description", certificate.getDescription())
                .addValue("price", certificate.getPrice())
                .addValue("duration", certificate.getDuration());
    }

    @Override
    protected SqlParameterSource getSqlParameterSourceForUpdate(GiftCertificate certificate) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", certificate.getId());

        Optional.ofNullable(certificate.getName()).ifPresent(s -> parameterSource.addValue("name", s));
        Optional.ofNullable(certificate.getDescription()).ifPresent(s -> parameterSource.addValue("description", s));
        Optional.ofNullable(certificate.getPrice()).ifPresent(s -> parameterSource.addValue("price", s));
        Optional.ofNullable(certificate.getDuration()).ifPresent(s -> parameterSource.addValue("duration", s));

        return parameterSource;
    }
}
