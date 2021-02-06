package com.epam.esm.service.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.JpaSpecification;
import com.epam.esm.repository.specification.impl.TagSpecification;
import com.epam.esm.service.converter.impl.TagConverterImpl;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TagServiceImplTest {

    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        tagService = new TagServiceImpl(tagRepository, new TagConverterImpl());

    }

    @Test
    void findOne() {
        TagDto expected = new TagDto("test-name", null, Boolean.FALSE, null);
        expected.setId(1L);
        Tag tag = new Tag("test-name", null, Boolean.FALSE, null);
        tag.setId(1L);

        when(tagRepository.findOne(1L)).thenReturn(Optional.of(tag));

        TagDto actual = tagService.findOne(1L);
        assertEquals(expected, actual);
    }

    @Test
    void query() {
        TagDto tagDto1 = new TagDto("test-name", null, Boolean.FALSE, null);
        TagDto tagDto2 = new TagDto("test-name-2", null, Boolean.FALSE, null);
        List<TagDto> expected = Arrays.asList(tagDto1, tagDto2);

        Tag tag1 = new Tag(tagDto1.getName(), null, tagDto1.getIsDeleted(), tagDto1.getDeleteDate());
        Tag tag2 = new Tag(tagDto2.getName(), null, tagDto2.getIsDeleted(), tagDto2.getDeleteDate());

        when(tagRepository.query(any(TagSpecification.class))).thenReturn(Arrays.asList(tag1, tag2));

        List<TagDto> actual = tagService.query(new TagSearchCriteriaDto("test", 1));
        assertEquals(expected, actual);
    }

    @Test
    void create() {
        Long expected = 1L;
        TagDto tagDto = new TagDto("test-name", null, Boolean.FALSE, null);
        Map<String, Object> constraints = new HashMap<>();
        constraints.putIfAbsent("name", tagDto.getName());

        when(tagRepository.exists(constraints)).thenReturn(Boolean.FALSE);
        when(tagRepository.create(new Tag(tagDto.getName(), null, tagDto.getIsDeleted(), tagDto.getDeleteDate())))
                .thenReturn(expected);

        Long actual = tagService.create(tagDto);
        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Long id = 112L;
        Tag sourceTag = new Tag("test-name", null, Boolean.FALSE, null);
        sourceTag.setId(id);
        TagDto updatingTagDto = new TagDto("test-name-UPDATED", null, Boolean.FALSE, null);
        updatingTagDto.setId(id);
        Tag updatingTag = new Tag("test-name-UPDATED", null, Boolean.FALSE, null);
        updatingTag.setId(id);

        when(tagRepository.findOne(id)).thenReturn(Optional.of(sourceTag));
        when(tagRepository.update(updatingTag)).thenReturn(Boolean.TRUE);

        boolean isUpdated = tagService.update(updatingTagDto);
        assertTrue(isUpdated);
    }

    @Test
    void delete() {
        Long id = 111L;
        Tag sourceTag = new Tag("test-name", null, Boolean.FALSE, null);
        sourceTag.setId(id);

        when(tagRepository.findOne(id)).thenReturn(Optional.of(sourceTag));
        when(tagRepository.delete(sourceTag.getId())).thenReturn(Boolean.TRUE);
        boolean isDeleted = tagService.delete(id);
        assertTrue(isDeleted);
    }

    @Test
    void deleteAssociationsWithRelatedEntitiesIfNecessary() {
    }

    @Test
    void receiveDomainWhichIsToBeUpdated() {
    }

    @Test
    void receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders() {
        Long id = 1743256L;
        Tag expectedTag = new Tag("tag", null, Boolean.TRUE, Instant.now());
        expectedTag.setId(id);
        TagDto expectedDto = new TagDto(expectedTag.getName(), null, expectedTag.getIsDeleted(),
                expectedTag.getDeleteDate());
        expectedDto.setId(expectedTag.getId());
        List<TagDto> expected = Collections.singletonList(expectedDto);

        when(tagRepository.receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders())
                .thenReturn(Collections.singletonList(expectedTag));

        List<TagDto> actual = tagService.receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders();
        assertEquals(expected, actual);
    }

    @Test
    void getDataSourceSpecification() {
        TagSearchCriteriaDto criteria = new TagSearchCriteriaDto("test", 123486);

        TagSpecification expected = new TagSpecification(criteria.getName(), criteria.getPage());
        JpaSpecification<Tag, Long> actual = tagService.getDataSourceSpecification(criteria);

        assertEquals(expected, actual);
    }

    @Test
    void receiveUpdatingDomain() {
        Long id = 437L;
        Tag sourceDomain = new Tag("name-1", null, Boolean.FALSE, null);
        sourceDomain.setId(id);
        TagDto updatingDto = new TagDto("name-1-UPDATED", null, Boolean.TRUE, Instant.now());
        Tag expected = new Tag("name-1-UPDATED", null, Boolean.FALSE, null);
        expected.setId(sourceDomain.getId());

        Tag actual = tagService.receiveUpdatingDomain(sourceDomain, updatingDto);
        assertEquals(expected, actual);
    }

    @Test
    void receiveUniqueConstraints() {
        TagDto tagDto = new TagDto("test-name", null, Boolean.FALSE, null);
        Map<String, Object> expected = new HashMap<>();
        expected.putIfAbsent("name", tagDto.getName());

        Map<String, Object> actual = tagService.receiveUniqueConstraints(tagDto);
        assertEquals(expected, actual);
    }
}