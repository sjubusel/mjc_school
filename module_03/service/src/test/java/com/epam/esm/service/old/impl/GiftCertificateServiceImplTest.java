package com.epam.esm.service.old.impl;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.old.specification.SqlSpecification;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.old.converter.GiftCertificateConverter;
import com.epam.esm.service.old.converter.TagConverter;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.service.exception.DuplicateResourceException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.old.validation.GiftCertificateServiceValidator;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Disabled
class GiftCertificateServiceImplTest {

    private GiftCertificateService giftCertificateService;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        giftCertificateService = new GiftCertificateServiceImpl(new GiftCertificateConverter(),
                giftCertificateRepository, tagRepository, new GiftCertificateServiceValidator(), new TagConverter());
    }

    @DisplayName("test creation of a new certificate with new tags")
    @Test
    void testCreateNewCertificateWithTags() {
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .setName("name")
                .setDescription("description")
                .setPrice(new BigDecimal(100))
                .setDuration(5)
                .setTags(new HashSet<>(Arrays.asList(new TagDto("tagName1", null), new TagDto("tagName2", null))))
                .build();

        when(giftCertificateRepository.exists(Mockito.anyMap())).thenReturn(false);
        when(giftCertificateRepository.create(Mockito.any(GiftCertificate.class))).thenReturn(999L);
        when(tagRepository.exists("tagName1")).thenReturn(false);
        when(tagRepository.exists("tagName2")).thenReturn(true);
        when(tagRepository.create(Mockito.any(Tag.class))).thenReturn(System.currentTimeMillis());
        doNothing().when(giftCertificateRepository).linkGiftCertificateWithTags(Mockito.anyLong(), Mockito.anyList());

        Long actual = giftCertificateService.create(giftCertificateDto);
        Assertions.assertEquals(999L, actual);
    }

    @DisplayName("test creation of a new certificate which already exists in the datasource")
    @Test
    void testCreateNewCertificateWhichAlreadyExists() {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        when(giftCertificateRepository.exists(Mockito.anyMap())).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceException.class, () -> giftCertificateService.create(giftCertificateDto));
    }

    @DisplayName("test creation of a new certificate with zero tags")
    @Test
    void testCreateNewCertificateWithZeroTags() {
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .setName("name")
                .setDescription("description")
                .setPrice(new BigDecimal(100))
                .setDuration(5)
                .build();
        when(giftCertificateRepository.exists(Mockito.anyMap())).thenReturn(false);
        when(giftCertificateRepository.create(Mockito.any(GiftCertificate.class))).thenReturn(999L);

        verify(tagRepository, never()).exists(anyString());
        verify(tagRepository, never()).create(any(Tag.class));
        verify(giftCertificateRepository, never()).linkGiftCertificateWithTags(Mockito.anyLong(), Mockito.anyList());

        Long actual = giftCertificateService.create(giftCertificateDto);
        Assertions.assertEquals(999L, actual);
    }

    @DisplayName("test read operation when there is nothing in the data source")
    @Test
    void queryAllWithoutResult() {
        when(giftCertificateRepository.query(any(SqlSpecification.class))).thenReturn(new ArrayList<>());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.query(null));
    }

    @DisplayName("test read operation when there is nothing in the data source")
    @Test
    void queryAllWithResultWithTags() {
        when(giftCertificateRepository.query(any(SqlSpecification.class)))
                .thenReturn(Arrays.asList(GiftCertificate.builder()
                        .setId(1L)
                        .setName("name")
                        .setDescription("description")
                        .setPrice(new BigDecimal(100))
                        .setDuration(5)
                        .build()));
        Tag tag = Tag.builder().setId(1L).setName("tagDtoName").build();
        when(tagRepository.receiveTagsByGiftCertificateId(Mockito.anyLong())).thenReturn(Arrays.asList(tag));
        List<GiftCertificateDto> actual = giftCertificateService.query(new GiftCertificateSearchCriteriaDto());

        GiftCertificateDto expected = GiftCertificateDto.builder()
                .setId(1L)
                .setName("name")
                .setDescription("description")
                .setPrice(new BigDecimal(100))
                .setDuration(5)
                .setTags(new HashSet<>(Arrays.asList(TagDto.builder().setId(1L).setName("tagDtoName").build())))
                .build();

        Assertions.assertEquals(expected, actual.get(0));

    }

    @Test
    void findOne() {
        when(giftCertificateRepository.findOne(1L)).thenReturn(Optional.of(GiftCertificate.builder()
                .setId(1L)
                .setName("name")
                .setDescription("description")
                .setPrice(new BigDecimal(100))
                .setDuration(5)
                .build()));
        when(tagRepository.receiveTagsByGiftCertificateId(1L)).thenReturn(new ArrayList<>());
        GiftCertificateDto expected = GiftCertificateDto.builder()
                .setId(1L)
                .setName("name")
                .setDescription("description")
                .setPrice(new BigDecimal(100))
                .setDuration(5)
                .setTags(new HashSet<>())
                .build();
        GiftCertificateDto actual = giftCertificateService.findOne(1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update() {
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .setId(1L)
                .setName("name")
                .setTags(new HashSet<>(Arrays.asList(new TagDto("tag1", null), new TagDto("tag2", null))))
                .build();
        when(giftCertificateRepository.findOne(1L)).thenReturn(Optional.of(GiftCertificate.builder()
                .setId(1L)
                .setName("nam")
                .build()));
        when(tagRepository.receiveTagsByGiftCertificateId(anyLong())).thenReturn(new ArrayList<>());
        when(tagRepository.exists(anyString())).thenReturn(true);
        when(tagRepository.create(any(Tag.class))).thenReturn(System.currentTimeMillis());
        doNothing().when(giftCertificateRepository).linkGiftCertificateWithTags(anyLong(), anyList());
        when(giftCertificateRepository.update(any(GiftCertificate.class))).thenReturn(true);
        Assertions.assertTrue(giftCertificateService.update(giftCertificateDto));
    }

}