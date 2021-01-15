package com.epam.esm.service.converter;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagConverter implements EntityConverter<TagDto, Tag, Long> {

    @Override
    public Tag convertToDomain(TagDto dto) {
        return Tag.builder().
                setId(dto.getId())
                .setName(dto.getName())
                .build();
    }

    @Override
    public TagDto convertToDto(Tag domain) {
        return TagDto.builder()
                .setId(domain.getId())
                .setName(domain.getName())
                .build();
    }

    @Override
    public Tag convertToUpdatingDomain(Tag sourceDomain, TagDto targetDto) {
        Tag updatingTag = new Tag();
        updatingTag.setId(targetDto.getId());

        if ((targetDto.getName() != null) && !targetDto.getName().equals(sourceDomain.getName())) {
            updatingTag.setName(targetDto.getName());
        }
        return updatingTag;
    }
}
