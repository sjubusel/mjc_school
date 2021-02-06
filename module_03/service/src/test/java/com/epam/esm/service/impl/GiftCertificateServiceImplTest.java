package com.epam.esm.service.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.GiftCertificateUpdateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.impl.GiftCertificateSpecification;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.converter.impl.GiftCertificateConverterImpl;
import com.epam.esm.service.converter.impl.TagConverter;
import com.epam.esm.service.converter.impl.TagConverterImpl;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class GiftCertificateServiceImplTest {

    private GiftCertificateService giftCertificateService;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        TagConverter tagConverter = new TagConverterImpl();
        GiftCertificateConverter giftCertificateConverter = new GiftCertificateConverterImpl(tagConverter) {
            @Override
            public GiftCertificate convertToUpdatingDomain(GiftCertificate sourceDomain) {
                GiftCertificate giftCertificate = super.convertToUpdatingDomain(sourceDomain);
                giftCertificate.setUpdateDate(Instant.parse("2020-12-02T11:11:11.156Z"));
                return giftCertificate;
            }
        };

        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, giftCertificateConverter,
                tagRepository, tagConverter);

    }

    @Test
    void findOne() {
        GiftCertificateDto expected = new GiftCertificateDto("test-name", "test-description", new BigDecimal("111.11"),
                12, Instant.parse("2020-12-02T11:11:11.156Z"), Instant.parse("2020-12-02T11:11:11.156Z"), null, false,
                null);
        expected.setId(1L);
        GiftCertificate expectedCertificate = new GiftCertificate(expected.getName(), expected.getDescription(),
                expected.getPrice(), expected.getDuration(), expected.getCreateDate(), expected.getUpdateDate(), null,
                expected.getIsDeleted(), expected.getDeleteDate());
        expectedCertificate.setId(1L);

        when(giftCertificateRepository.findOne(anyLong())).thenReturn(Optional.of(expectedCertificate));

        GiftCertificateDto actual = giftCertificateService.findOne(1L);
        assertEquals(expected, actual);
    }

    @Test
    void query() {
        GiftCertificate expectedDomain = new GiftCertificate("test-name", "test-description",
                new BigDecimal("111.11"), 12, Instant.parse("2020-12-02T11:11:11.156Z"),
                Instant.parse("2020-12-02T11:11:11.156Z"), null, false, null);
        expectedDomain.setId(1L);
        List<GiftCertificate> expectedDomainList = Collections.singletonList(expectedDomain);
        GiftCertificateDto expectedDto = new GiftCertificateDto("test-name", "test-description",
                new BigDecimal("111.11"), 12, Instant.parse("2020-12-02T11:11:11.156Z"),
                Instant.parse("2020-12-02T11:11:11.156Z"), null, false, null);
        expectedDto.setId(1L);
        List<GiftCertificateDto> expected = Collections.singletonList(expectedDto);

        when(giftCertificateRepository.query(any(GiftCertificateSpecification.class)))
                .thenReturn(expectedDomainList);

        List<GiftCertificateDto> actual = giftCertificateService.query(new GiftCertificateSearchCriteriaDto(null,
                "test-name", "test-description", Collections.singletonList("+name"), null));
        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        GiftCertificate expectedDomain = new GiftCertificate("test-name", "test-description",
                new BigDecimal("111.11"), 12, Instant.parse("2020-12-02T11:11:11.156Z"),
                Instant.parse("2020-12-02T11:11:11.156Z"), null, false, null);
        expectedDomain.setId(1L);

        when(giftCertificateRepository.findOne(anyLong())).thenReturn(Optional.of(expectedDomain));
        when(giftCertificateRepository.delete(1L)).thenReturn(true);

        boolean isDeleted = giftCertificateService.delete(1L);
        assertTrue(isDeleted);
    }

    @Test
    void create() {
        TagDto existingTagDto = new TagDto("test-tag-name-1", null, Boolean.FALSE, null);
        existingTagDto.setId(1L);
        TagDto nonExistingTagDto = new TagDto("test-tag-name-2", null, Boolean.FALSE, null);
        nonExistingTagDto.setId(2L);
        GiftCertificateDto dto = new GiftCertificateDto("test-name", "test-description", new BigDecimal("111.11"),
                12, Instant.parse("2020-12-02T11:11:11.156Z"), Instant.parse("2020-12-02T11:11:11.156Z"),
                new HashSet<>(Arrays.asList(existingTagDto, nonExistingTagDto)), Boolean.FALSE, null);
        dto.setId(1L);

        Map<String, Object> uniqueConstraints = new HashMap<>();
        uniqueConstraints.putIfAbsent("name", dto.getName());
        uniqueConstraints.putIfAbsent("description", dto.getDescription());
        uniqueConstraints.putIfAbsent("price", dto.getPrice());
        uniqueConstraints.putIfAbsent("duration", dto.getDuration());

        Tag existingTag = new Tag(existingTagDto.getName(), null, existingTagDto.getIsDeleted(),
                existingTagDto.getDeleteDate());
        existingTag.setId(existingTagDto.getId());
        Tag nonExistingTag = new Tag(nonExistingTagDto.getName(), null, nonExistingTagDto.getIsDeleted(),
                nonExistingTagDto.getDeleteDate());
        nonExistingTag.setId(nonExistingTagDto.getId());
        GiftCertificate domain = new GiftCertificate(dto.getName(), dto.getDescription(), dto.getPrice(),
                dto.getDuration(), dto.getCreateDate(), dto.getUpdateDate(),
                new HashSet<>(Arrays.asList(existingTag, nonExistingTag)), Boolean.FALSE, null);
        domain.setId(1L);

        when(giftCertificateRepository.exists(uniqueConstraints)).thenReturn(Boolean.FALSE);
        when(giftCertificateRepository.create(domain)).thenReturn(1L);
        when(tagRepository.exists(existingTag.getName())).thenReturn(Boolean.TRUE);
        when(tagRepository.exists(nonExistingTag.getName())).thenReturn(Boolean.FALSE);
        doNothing().when(tagRepository).refreshStateOfTagsByTheirName(Collections.singletonList(existingTag));
        when(tagRepository.create(nonExistingTag)).thenReturn(2L);
        doNothing().when(giftCertificateRepository).linkGiftCertificateWithTags(1L, domain.getTags());

        Long actual = giftCertificateService.create(dto);
        assertEquals(1L, actual);
    }

    @Test
    void update() {
        TagDto existingTagDto = new TagDto("test-tag-name-1", null, Boolean.FALSE, null);
        existingTagDto.setId(1L);
        TagDto nonExistingTagDto = new TagDto("test-tag-name-2", null, Boolean.FALSE, null);
        nonExistingTagDto.setId(2L);

        GiftCertificateUpdateDto updateDto = new GiftCertificateUpdateDto();
        updateDto.setId(1L);
        updateDto.setPrice(new BigDecimal("1.1"));
        updateDto.setTags(new HashSet<>(Arrays.asList(existingTagDto, nonExistingTagDto)));

        Tag existingTag = new Tag(existingTagDto.getName(), null, existingTagDto.getIsDeleted(),
                existingTagDto.getDeleteDate());
        existingTag.setId(existingTagDto.getId());
        Tag nonExistingTag = new Tag(nonExistingTagDto.getName(), null, nonExistingTagDto.getIsDeleted(),
                nonExistingTagDto.getDeleteDate());
        nonExistingTag.setId(nonExistingTagDto.getId());
        Tag domainTag = new Tag("domain-tag", null, Boolean.FALSE, null);
        domainTag.setId(3L);
        GiftCertificate sourceDomain = new GiftCertificate("test-name", "test-description",
                new BigDecimal("1.20"), 12, Instant.parse("2020-12-02T11:11:11.156Z"),
                Instant.parse("2020-12-02T11:11:11.156Z"), new HashSet<>(Collections.singletonList(domainTag)),
                Boolean.FALSE, null);
        sourceDomain.setId(1L);

        when(giftCertificateRepository.findOne(1L)).thenReturn(Optional.of(sourceDomain)); // sourceDomain
        when(tagRepository.exists(existingTagDto.getName())).thenReturn(Boolean.TRUE);
        when(tagRepository.exists(nonExistingTagDto.getName())).thenReturn(Boolean.FALSE);
        doNothing().when(tagRepository).refreshStateOfTagsByTheirName(Collections.singletonList(existingTag));
        when(tagRepository.create(nonExistingTag)).thenReturn(2L);
        doNothing().when(giftCertificateRepository).linkGiftCertificateWithTags(1L,
                new HashSet<>(Arrays.asList(domainTag, existingTag, nonExistingTag)));

        GiftCertificate targetDomain = new GiftCertificate(sourceDomain.getName(), sourceDomain.getDescription(),
                updateDto.getPrice(), sourceDomain.getDuration(), sourceDomain.getCreateDate(),
                Instant.parse("2020-12-02T11:11:11.156Z"),
                new HashSet<>(Arrays.asList(domainTag, existingTag, nonExistingTag)), Boolean.FALSE, null);
        targetDomain.setId(1L);
        when(giftCertificateRepository.update(targetDomain)).thenReturn(Boolean.TRUE);

        boolean isUpdated = giftCertificateService.update(updateDto);
        assertTrue(isUpdated);
    }
}