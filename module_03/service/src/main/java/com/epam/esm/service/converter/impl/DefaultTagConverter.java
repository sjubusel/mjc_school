package com.epam.esm.service.converter.impl;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.converter.GeneralEntityConverter;
import com.googlecode.jmapper.JMapper;

public class DefaultTagConverter implements GeneralEntityConverter<TagDto, Tag, Long> {

    private JMapper<Tag, TagDto> mapperToDomain;
    private JMapper<TagDto, Tag> mapperToDto;

    public DefaultTagConverter() {
        this.mapperToDomain = new JMapper<>(Tag.class, TagDto.class);
        this.mapperToDto = new JMapper<>(TagDto.class, Tag.class);
    }

    @Override
    public Tag convertToDomain(TagDto dto) {
        return null;
    }

    @Override
    public TagDto convertToDto(Tag tag) {
        return null;
    }
}
