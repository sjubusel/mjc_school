package com.epam.esm.web.util.impl;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.web.controller.OrderController;
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

@Component
@RequiredArgsConstructor
public class OrderHateoasActionsAppender implements HateoasActionsAppender<Long, OrderDto> {

    private final PagedResourcesAssembler<OrderDto> pagedResourcesAssembler;

    @Override
    public void appendSelfReference(OrderDto dto) {
        dto.add(linkTo(OrderController.class).slash(dto.getId()).withSelfRel());
    }

    @Override
    public void appendAsForMainEntity(OrderDto dto) {
        appendAsForSecondaryEntity(dto);
        appendGenericOrderHateoasActions(dto);
    }

    @Override
    public void appendAsForSecondaryEntity(OrderDto dto) {
        appendSelfReference(dto);
    }

    @Override
    public CollectionModel<EntityModel<OrderDto>> toHateoasCollectionOfEntities(Page<OrderDto> orders) {
        orders.forEach(this::appendSelfReference);
        PagedModel<EntityModel<OrderDto>> collectionModel = pagedResourcesAssembler.toModel(orders);
        appendGenericOrderHateoasActions(collectionModel);
        return collectionModel;
    }

    @SuppressWarnings("rawtypes")
    private void appendGenericOrderHateoasActions(RepresentationModel dto) {
        dto.add(linkTo(OrderController.class).withRel("POST: create a new order"));
        dto.add(linkTo(OrderController.class).withRel("GET: get all orders"));
    }
}
