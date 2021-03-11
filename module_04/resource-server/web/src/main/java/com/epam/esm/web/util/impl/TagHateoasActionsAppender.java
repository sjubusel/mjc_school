package com.epam.esm.web.util.impl;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.util.HateoasActionsAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class TagHateoasActionsAppender implements HateoasActionsAppender<Long, TagDto> {

    private final PagedResourcesAssembler<TagDto> pagedResourcesAssembler;

    @Override
    public void appendSelfReference(TagDto tagDto) {
        tagDto.add(linkTo(TagController.class).slash(tagDto.getId()).withSelfRel());
    }

    @Override
    public void appendAsForMainEntity(TagDto dto) {
        appendAsForSecondaryEntity(dto);
        appendGenericCreateAndReadAllHateoasActions(dto);
        appendReferenceForMainTag(dto);
    }

    @Override
    public void appendAsForSecondaryEntity(TagDto dto) {
        appendSelfReference(dto);
        dto.add(linkTo(TagController.class).slash(dto.getId()).withRel("PUT: update a current tag"));
        dto.add(linkTo(TagController.class).slash(dto.getId()).withRel("DELETE: delete a current tag"));
    }

    @Override
    public CollectionModel<EntityModel<TagDto>> toHateoasCollectionOfEntities(Page<TagDto> tags) {
        tags.forEach(this::appendAsForSecondaryEntity);
        PagedModel<EntityModel<TagDto>> collectionModel = pagedResourcesAssembler.toModel(tags);
        appendGenericCreateAndReadAllHateoasActions(collectionModel);
        appendReferenceForMainTag(collectionModel);
        return collectionModel;
    }

    @SuppressWarnings("rawtypes")
    private void appendGenericCreateAndReadAllHateoasActions(RepresentationModel model) {
        model.add(linkTo(TagController.class).withRel("POST: create a new tag"));
        model.add(linkTo(TagController.class).withRel("GET: receive all tags"));
    }

    @SuppressWarnings("rawtypes")
    private void appendReferenceForMainTag(RepresentationModel model) {
        model.add(linkTo(methodOn(TagController.class).receiveMainTag()).withRel("GET: receive the main tag"));
    }
}
