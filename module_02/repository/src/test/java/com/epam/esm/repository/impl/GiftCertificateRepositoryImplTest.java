package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.configuration.TestRepositoryConfiguration;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
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

    static {
        FIRST_CERTIFICATE.setId(1L);
        SECOND_CERTIFICATE.setId(2L);
        THIRD_CERTIFICATE.setId(3L);
        FORTH_CERTIFICATE.setId(4L);
        FIFTH_CERTIFICATE.setId(5L);
    }
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void name() {
        Optional<Tag> one = tagRepository.findOne(1L);
        assertEquals("Развлечения", one.orElse(new Tag()).getName());
    }

    @DisplayName("test READ all without parameters operation")
    @Test
    public void testQuery() {
        List<GiftCertificate> expected = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE,
                FORTH_CERTIFICATE, FIFTH_CERTIFICATE);
        List<GiftCertificate> actual = (List<GiftCertificate>) giftCertificateRepository
                .query(new GiftCertificateSpecification());
        assertEquals(expected, actual);
    }
}
