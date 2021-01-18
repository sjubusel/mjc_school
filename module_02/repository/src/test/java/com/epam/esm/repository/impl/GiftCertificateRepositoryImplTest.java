package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.configuration.TestRepositoryConfiguration;
import com.epam.esm.repository.mapper.GiftCertificateMapper;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
@ActiveProfiles("test")
class GiftCertificateRepositoryImplTest {

    private static final GiftCertificate FIRST_CERTIFICATE = new GiftCertificate("Скалодром", "Скалодром на " +
            "Партизанском", new BigDecimal("30.00"), 5, Instant.ofEpochSecond(1L), Instant.ofEpochSecond(1L), null);
    private static final GiftCertificate SECOND_CERTIFICATE = new GiftCertificate("Одежда в подарок", "H&M на Немиге",
            new BigDecimal("300.00"), 10, Instant.ofEpochSecond(1L), Instant.ofEpochSecond(1L), null);
    private static final GiftCertificate THIRD_CERTIFICATE = new GiftCertificate("Картинг", "Картинг с самым " +
            "большим треком в Республике Беларусь", new BigDecimal("50.00"), 5, Instant.ofEpochSecond(1L),
            Instant.ofEpochSecond(1L), null);
    private static final GiftCertificate FORTH_CERTIFICATE = new GiftCertificate("Пицца за 30%", "Скидки 30% на весь " +
            "перечень продукции в сети пиццерий Pizza ", new BigDecimal("1.50"), 7, Instant.ofEpochSecond(1L),
            Instant.ofEpochSecond(1L), null);
    private static final GiftCertificate FIFTH_CERTIFICATE = new GiftCertificate("Мобильный телефон за старый",
            "Покупай новый флагман (мобильный телефон) за 100 рублей при сдаче более старого флагмана того же " +
                    "производителя", new BigDecimal("200.00"), 5, Instant.ofEpochSecond(1L), Instant.ofEpochSecond(1L),
            null);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static {
        FIRST_CERTIFICATE.setId(1L);
        SECOND_CERTIFICATE.setId(2L);
        THIRD_CERTIFICATE.setId(3L);
        FORTH_CERTIFICATE.setId(4L);
        FIFTH_CERTIFICATE.setId(5L);
    }

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @DisplayName("test READ all operation without parameters")
    @Order(1)
    @Test
    void testQueryWithEmptySqlSpecification() {
        List<GiftCertificate> expected = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE,
                FORTH_CERTIFICATE, FIFTH_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository
                .query(new GiftCertificateSpecification());
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by parameter (:name)")
    @Order(2)
    @Test
    void testQueryWithSearchByName() {
        List<GiftCertificate> expected = Collections.singletonList(FIRST_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(null, "лодром", null, null, null));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by parameter (:description)")
    @Order(3)
    @Test
    void testQueryWithSearchByDescription() {
        List<GiftCertificate> expected = Collections.singletonList(SECOND_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(null, null, "H&M", null, null));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by parameters (:description, :name)")
    @Order(4)
    @Test
    void testQueryWithSearchByNameAndDescription() {
        List<GiftCertificate> expected = Collections.singletonList(THIRD_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(null, "Картинг", "Картинг", null, null));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by parameter (:tag)")
    @Order(5)
    @Test
    void testQueryWithSearchByTag() {
        List<GiftCertificate> expected = Arrays.asList(FORTH_CERTIFICATE, FIFTH_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(Collections.singletonList("Скидки"), null, null, null, null));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by parameters (:tags)")
    @Order(6)
    @Test
    void testQueryWithSearchByTags() {
        List<GiftCertificate> expected = Arrays.asList(FIRST_CERTIFICATE, THIRD_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(Arrays.asList("Развлечения", "Активный отдых"), null, null, null, null));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by parameter (:orderParam): no ordering applied because of absence of sortParams")
    @Order(7)
    @Test
    void testQueryWithSearchByAscendingOrderParamNoResult() {
        List<GiftCertificate> expected = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE,
                FORTH_CERTIFICATE, FIFTH_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(null, null, null, null, "ASC"));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by parameter (:orderParam): no ordering applied because of absence of sortParams")
    @Order(8)
    @Test
    void testQueryWithSearchByDescendingOrderParamNoResult() {
        List<GiftCertificate> expected = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE,
                FORTH_CERTIFICATE, FIFTH_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(null, null, null, null, "DESC"));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by parameters (:orderParam, :sortParam - name)")
    @Order(9)
    @Test
    void testQueryWithSearchByDescendingOrderParamAndSortName() {
        List<GiftCertificate> expected = Arrays.asList(FIRST_CERTIFICATE, FORTH_CERTIFICATE, SECOND_CERTIFICATE,
                FIFTH_CERTIFICATE, THIRD_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(null, null, null, Collections.singletonList("name"), "DESC"));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by all parameters")
    @Order(10)
    @Test
    void testQueryWithSearchByParamsTogether() {
        List<GiftCertificate> expected = Collections.singletonList(FIRST_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(Arrays.asList("Развлечения", "Активный отдых"), "ка", "ка",
                        Collections.singletonList("name"), "ASC"));
        assertEquals(expected, actual);
    }

    @DisplayName("test READ operation by all parameters with no result")
    @Order(11)
    @Test
    void testQueryWithSearchByParamsTogetherWithNoResult() {
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository.query(
                new GiftCertificateSpecification(Arrays.asList("Развлечения", "Активный отдых"), "к$а", "ка",
                        Collections.singletonList("name"), "ASC"));
        assertEquals(0, actual.size());
    }

    @DisplayName("test CREATE operation")
    @Order(12)
    @Test
    void testCreate() {
        GiftCertificate expected = new GiftCertificate("name", "description", new BigDecimal("1.00"), 1,
                Instant.now(), Instant.now(), null);
        assertThrows(ClassCastException.class, () -> giftCertificateRepository.create(expected));
        expected.setId(6L);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", expected.getName())
                .addValue("description", expected.getDescription())
                .addValue("price", expected.getPrice())
                .addValue("duration", expected.getDuration())
                .addValue("createDate", Timestamp.from(expected.getCreateDate()))
                .addValue("lastUpdateDate", Timestamp.from(expected.getUpdateDate()));

        List<GiftCertificate> actual = namedParameterJdbcTemplate.query("SELECT * FROM " +
                        "gift_certificates_system.certificates " +
                        "WHERE name = :name " +
                        "AND  description = :description " +
                        "AND price = :price " +
                        "AND duration = :duration " +
                        "AND create_date = :createDate " +
                        "AND last_update_date = :lastUpdateDate",
                parameterSource, new GiftCertificateMapper());

        assertEquals(expected, actual.get(0));
    }

    @DisplayName("test READ operation by resource's id")
    @Order(13)
    @Test
    void testFindOne() {
        Optional<GiftCertificate> actual = giftCertificateRepository.findOne(4L);

        assertEquals(FORTH_CERTIFICATE, actual.orElse(null));
    }

    @DisplayName("test UPDATE operation")
    @Order(14)
    @Test
    void testUpdate() {
        Instant updatingDatetime = Instant.now();
        GiftCertificate updatingCertificate = GiftCertificate.builder()
                .setId(5L)
                .setName("Мобильный телефон за старый UPDATED")
                .setPrice(new BigDecimal("1.96"))
                .setUpdateDate(updatingDatetime)
                .build();
        boolean isUpdated = giftCertificateRepository.update(updatingCertificate);
        assertTrue(isUpdated);
        List<GiftCertificate> expected = namedParameterJdbcTemplate.query("SELECT * " +
                        "FROM gift_certificates_system.certificates WHERE certificate_id = :id",
                new MapSqlParameterSource("id", 5L), new GiftCertificateMapper());

        FIFTH_CERTIFICATE.setName("Мобильный телефон за старый UPDATED");
        FIFTH_CERTIFICATE.setPrice(new BigDecimal("1.96"));
        FIFTH_CERTIFICATE.setUpdateDate(updatingDatetime);
        assertEquals(expected.get(0), FIFTH_CERTIFICATE);
    }

    @Order(15)
    @Test
    void name() {
        Long idToDelete = 5L;
        namedParameterJdbcTemplate.update("DELETE FROM gift_certificates_system.join_certificates_tags_table " +
                "WHERE certificate_id = :id", new MapSqlParameterSource("id", idToDelete));
        boolean isDeleted = giftCertificateRepository.delete(idToDelete);
        assertTrue(isDeleted);

        List<GiftCertificate> actual = namedParameterJdbcTemplate.query("SELECT * " +
                        "FROM gift_certificates_system.certificates WHERE certificate_id = :id",
                new MapSqlParameterSource("id", idToDelete), new GiftCertificateMapper());

        assertTrue(actual.isEmpty());
    }
}
