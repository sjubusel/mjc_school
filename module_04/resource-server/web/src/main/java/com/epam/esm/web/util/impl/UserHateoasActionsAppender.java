package com.epam.esm.web.util.impl;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.util.HateoasActionsAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class UserHateoasActionsAppender implements HateoasActionsAppender<Long, UserDto> {

    private final OrderHateoasActionsAppender orderHateoasActionsAppender;
    private final PagedResourcesAssembler<UserDto> pagedResourcesAssembler;

    @Override
    public void appendSelfReference(UserDto dto) {
        dto.add(linkTo(UserController.class).slash(dto.getId()).withSelfRel());
    }

    @Override
    public void appendAsForMainEntity(UserDto dto) {
        appendAsForSecondaryEntity(dto);
        appendGenericUserHateoasActions(dto);
    }

    @Override
    public void appendAsForSecondaryEntity(UserDto dto) {
        appendSelfReference(dto);
        dto.add(linkTo(methodOn(UserController.class).createOrder(dto.getId(), new OrderDto()))
                .withRel("POST: create a new order for a current user"));
        dto.add(linkTo(methodOn(UserController.class).readOrders(dto.getId(), new OrderSearchCriteriaDto(), null, null))
                .withRel("GET: receive all orders for a current user"));
        dto.add(linkTo(methodOn(UserController.class).readOrder(dto.getId(), 0L))
                .withRel("GET: receive an order with a number instead of 0 if it belongs to a current user"));
    }

    @Override
    public CollectionModel<EntityModel<UserDto>> toHateoasCollectionOfEntities(Page<UserDto> users) {
        users.forEach(this::appendAsForSecondaryEntity);
        PagedModel<EntityModel<UserDto>> collectionModel = pagedResourcesAssembler.toModel(users);
        appendGenericUserHateoasActions(collectionModel);
        Link postLink = linkTo(methodOn(UserController.class).createOrder(0L, new OrderDto()))
                .withRel("POST: create a new order for a user with id 0 (change)");
        return collectionModel.add(postLink);
    }

    @SuppressWarnings("rawtypes")
    private void appendGenericUserHateoasActions(RepresentationModel dto) {
        dto.add(linkTo(UserController.class).withRel("GET: receive all users"));
    }

    public CollectionModel<EntityModel<OrderDto>> toHateoasCollectionOfOrders(Page<OrderDto> orders) {
        return orderHateoasActionsAppender.toHateoasCollectionOfEntities(orders);
    }
}
