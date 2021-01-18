package com.epam.esm.repository.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.configuration.TestRepositoryConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
@ActiveProfiles("test")
class GiftCertificateRepositoryImplTest {
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

    }


}