package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.configuration.TestRepositoryConfiguration;
import com.epam.esm.repository.mapper.TagMapper;
import com.epam.esm.repository.specification.impl.TagSpecification;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
@ActiveProfiles("test")
class TagRepositoryImplTest {

    public static final Tag FIRST_TAG = new Tag("Развлечения");
    public static final Tag SECOND_TAG = new Tag("Активный отдых");
    public static final Tag THIRD_TAG = new Tag("Скидки");
    public static final Tag FORTH_TAG = new Tag("Продовольственные товары");
    public static final Tag FIFTH_TAG = new Tag("Непродовольственные товары");
    public static final Tag SIXTH_TAG = new Tag("Техника");
    public static final Tag SEVENTH_TAG = new Tag("Мода");

    static {
        FIRST_TAG.setId(1L);
        SECOND_TAG.setId(2L);
        THIRD_TAG.setId(3L);
        FORTH_TAG.setId(4L);
        FIFTH_TAG.setId(5L);
        SIXTH_TAG.setId(6L);
        SEVENTH_TAG.setId(7L);
    }

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private TagMapper mapper;

    @Order(1)
    @Test
    void testQueryAllTags() {
        List<Tag> expected = Arrays.asList(FIRST_TAG, SECOND_TAG, THIRD_TAG, FORTH_TAG, FIFTH_TAG, SIXTH_TAG,
                SEVENTH_TAG);
        List<Tag> actual = (List<Tag>) tagRepository.query(new TagSpecification());

        assertEquals(expected, actual);
    }

    @Order(2)
    @Test
    void testQueryTagsByParameter() {
        List<Tag> expected = Arrays.asList(FORTH_TAG, FIFTH_TAG);
        List<Tag> actual = (List<Tag>) tagRepository.query(new TagSpecification("одовольстве"));

        assertEquals(expected, actual);
    }

    @Order(3)
    @Test
    void testFindOne() {
        Optional<Tag> actual = tagRepository.findOne(6L);

        assertEquals(SIXTH_TAG, actual.orElse(FIRST_TAG));
    }

    @Order(4)
    @Test
    void testDelete() {
        Long idToDelete = 1L;
        namedParameterJdbcTemplate.update("DELETE FROM gift_certificates_system.join_certificates_tags_table " +
                "WHERE tag_id = :id", new MapSqlParameterSource("id", idToDelete));
        boolean isDeleted = tagRepository.delete(idToDelete);
        assertTrue(isDeleted);

        List<Tag> actual = namedParameterJdbcTemplate.query("SELECT * " +
                        "FROM gift_certificates_system.tags WHERE tag_id = :id",
                new MapSqlParameterSource("id", idToDelete), mapper);

        assertTrue(actual.isEmpty());
    }

    @Test
    void testCreate() {
        Tag expected = new Tag("new tag");
        assertThrows(ClassCastException.class, () -> tagRepository.create(expected));
        expected.setId(8L);

        List<Tag> actual = namedParameterJdbcTemplate.query("SELECT * FROM gift_certificates_system.tags WHERE name = :name",
                new MapSqlParameterSource("name", expected.getName()), mapper);

        assertEquals(expected, actual.get(0));
    }
}