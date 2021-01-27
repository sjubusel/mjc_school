package com.epam.esm.repository.old.mapper;

import com.epam.esm.model.domain.GiftCertificate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * a class which maps any com.epam.esm.model.domain.GiftCertificate object and its database analogy
 * while using org.springframework.jdbc.core.JdbcTemplate
 */
public class GiftCertificateMapper implements EntityMapper<GiftCertificate, Long> {

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();

        certificate.setId(rs.getLong("id"));
        certificate.setName(rs.getString("name"));
        certificate.setDescription(rs.getString("description"));
        certificate.setPrice(rs.getObject("price", BigDecimal.class));
        certificate.setDuration(rs.getInt("duration"));
        Timestamp createDate = rs.getObject("create_date", Timestamp.class);
        certificate.setCreateDate(createDate.toInstant());
        Timestamp lastUpdateDate = rs.getObject("last_update_date", Timestamp.class);
        certificate.setUpdateDate(lastUpdateDate.toInstant());

        return certificate;
    }
}
