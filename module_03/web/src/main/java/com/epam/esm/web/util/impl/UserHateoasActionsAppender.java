package com.epam.esm.web.util.impl;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.dto.OrderSearchCriteriaDto;
import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.util.HateoasActionsAppender;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasActionsAppender implements HateoasActionsAppender<Long, UserDto> {

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
        dto.add(linkTo(methodOn(UserController.class).readOrders(dto.getId(), new OrderSearchCriteriaDto()))
                .withRel("GET: receive all orders for a current user"));
        dto.add(linkTo(methodOn(UserController.class).readOrder(dto.getId(), 0L))
                .withRel("GET: receive an order with a number instead of 0 if it belongs to a current user"));
    }

    @Override
    public CollectionModel<UserDto> toHateoasCollectionOfEntities(List<UserDto> users) {
        users.forEach(this::appendAsForSecondaryEntity);
        Link selfLink = linkTo(UserController.class).withSelfRel();
        CollectionModel<UserDto> collectionModel = CollectionModel.of(users, selfLink);
        appendGenericUserHateoasActions(collectionModel);
        Link postLink = linkTo(methodOn(UserController.class).createOrder(0L, new OrderDto()))
                .withRel("POST: create a new order for a user with id 0 (change)");
        return collectionModel.add(postLink);
    }

    @SuppressWarnings("rawtypes")
    private void appendGenericUserHateoasActions(RepresentationModel dto) {
        dto.add(linkTo(UserController.class).withRel("GET: receive all users"));
    }
}
