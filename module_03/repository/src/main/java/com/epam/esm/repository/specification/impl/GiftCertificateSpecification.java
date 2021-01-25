package com.epam.esm.repository.specification.impl;

import com.epam.esm.repository.specification.SqlSpecification;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class GiftCertificateSpecification implements SqlSpecification {

    private static final String SELECT_GIFT_CERTIFICATES = "SELECT DISTINCT certificate.certificate_id, " +
            "certificate.name, certificate.description, certificate.price, certificate.duration, " +
            "certificate.create_date, certificate.last_update_date " +
            "FROM gift_certificates_system.certificates certificate ";
    private static final String WHITESPACE = " ";
    private static final String PERCENT_SYMBOL = "%";

    private MapSqlParameterSource parameterSource;

    private List<String> joinBlock;
    private List<String> whereBlock;
    private List<String> sortBlock;
    private String orderBlock;


    public GiftCertificateSpecification() {
    }

    public GiftCertificateSpecification(List<String> searchTags, String searchName, String searchDescription,
                                        List<String> sortParams, String orderParam) {
        if (Stream.of(searchTags, searchName, searchDescription, sortParams, orderParam).allMatch(Objects::isNull)) {
            return;
        }

        processTags(searchTags);
        processGiftCertificateNamePart(searchName);
        processGiftCertificateDescriptionPart(searchDescription);
        processSortingParametersAndOrder(sortParams, orderParam);
    }

    @Override
    public String toSql() {
        return Stream.of(joinBlock, whereBlock, sortBlock).anyMatch(Objects::nonNull)
                ? generateQuery()
                : SELECT_GIFT_CERTIFICATES;
    }

    @Override
    public SqlParameterSource params() {
        return parameterSource;
    }

    private void processTags(List<String> searchTags) {
        if (searchTags != null && searchTags.size() > 0) {
            joinBlock = createIfNotExists(joinBlock);
            joinBlock.add("LEFT OUTER JOIN  gift_certificates_system.join_certificates_tags_table join_table " +
                    "ON certificate.certificate_id = join_table.certificate_id");
            joinBlock.add("LEFT OUTER JOIN gift_certificates_system.tags tag ON join_table.tag_id = tag.tag_id");

            whereBlock = createIfNotExists(whereBlock);
            whereBlock.add("tag.name IN (:tags)");

            parameterSource = createIfNotExists(parameterSource);
            parameterSource.addValue("tags", searchTags);
        }
    }

    private void processGiftCertificateNamePart(String searchName) {
        if (searchName != null && !searchName.isEmpty()) {
            whereBlock = createIfNotExists(whereBlock);
            whereBlock.add("certificate.name LIKE :name");

            parameterSource = createIfNotExists(parameterSource);
            parameterSource.addValue("name", generatePatternForLikeOperator(searchName));
        }
    }

    private void processGiftCertificateDescriptionPart(String searchDescription) {
        if (searchDescription != null && !searchDescription.isEmpty()) {
            whereBlock = createIfNotExists(whereBlock);
            whereBlock.add("certificate.description LIKE :description");

            parameterSource = createIfNotExists(parameterSource);
            parameterSource.addValue("description", generatePatternForLikeOperator(searchDescription));
        }
    }

    private List<String> createIfNotExists(List<String> block) {
        return Optional.ofNullable(block).orElseGet(ArrayList::new);
    }

    private MapSqlParameterSource createIfNotExists(MapSqlParameterSource parameterSource) {
        return Optional.ofNullable(parameterSource).orElseGet(MapSqlParameterSource::new);
    }

    private void processSortingParametersAndOrder(List<String> sortParams, String orderParam) {
        if (sortParams != null && sortParams.size() > 0) {
            sortBlock = createIfNotExists(sortBlock);

            for (String sortParam : sortParams) {
                String someParam = String.format("certificate.%s", sortParam);
                sortBlock.add(someParam);
            }

            orderBlock = ((orderParam != null) && orderParam.equalsIgnoreCase("DESC")) ? "DESC" : "ASC";
        }
    }

    private String generatePatternForLikeOperator(String sqlTextPart) {
        return PERCENT_SYMBOL + sqlTextPart + PERCENT_SYMBOL;
    }

    private String generateQuery() {
        StringBuilder sb = new StringBuilder(SELECT_GIFT_CERTIFICATES);
        sb.append(WHITESPACE);

        if (joinBlock != null && joinBlock.size() > 0) {
            joinBlock.forEach(join -> sb.append(join).append(WHITESPACE));
        }

        if (whereBlock != null && whereBlock.size() > 0) {
            sb.append("WHERE ");

            String whereDelimiter = "";
            for (String whereStatement : whereBlock) {
                sb.append(whereDelimiter).append(whereStatement).append(WHITESPACE);
                whereDelimiter = "AND ";
            }
        }

        if (sortBlock != null && sortBlock.size() > 0) {
            sb.append("ORDER BY ");

            String sortDelimiter = "";
            for (String sortParam : sortBlock) {
                sb.append(sortDelimiter).append(sortParam);
                sortDelimiter = ", ";
            }

            sb.append(WHITESPACE).append(orderBlock);
        }

        return new String(sb);
    }
}
