package com.epam.esm.web.util.impl;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.util.HateoasActionsAppender;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class TagHateoasActionsAppender implements HateoasActionsAppender<Long, TagDto> {

    @Override
    public void appendSelfReference(TagDto tagDto) {
        tagDto.add(linkTo(TagController.class).slash(tagDto.getId()).withSelfRel());
    }

    @Override
    public void appendAsForMainEntity(TagDto dto) {

    }

    @Override
    public void appendAsForSecondaryEntity(TagDto dto) {

    }

    @Override
    public CollectionModel<TagDto> toHateoasCollectionOfEntities(List<TagDto> dtoList) {
        return null;
    }
}
